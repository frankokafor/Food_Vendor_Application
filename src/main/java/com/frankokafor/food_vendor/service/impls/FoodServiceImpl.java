package com.frankokafor.food_vendor.service.impls;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.frankokafor.food_vendor.exceptions.UserServiceException;
import com.frankokafor.food_vendor.messages.ErrorMessages;
import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.messages.NotificationMessages;
import com.frankokafor.food_vendor.models.Food;
import com.frankokafor.food_vendor.models.Roles;
import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.repositories.FoodRepository;
import com.frankokafor.food_vendor.repositories.RolesRepository;
import com.frankokafor.food_vendor.repositories.UserRepository;
import com.frankokafor.food_vendor.request.objects.FoodDetailsRequest;
import com.frankokafor.food_vendor.response.objects.FoodDetailsResponse;
import com.frankokafor.food_vendor.security.AuthenticatedUserFacade;
import com.frankokafor.food_vendor.services.FoodService;
import com.frankokafor.food_vendor.services.NotificationService;
import com.frankokafor.food_vendor.transferObjects.FoodDTO;
import com.frankokafor.food_vendor.utilities.FunctionUtils;

@Service
public class FoodServiceImpl implements FoodService {
	@Value("${file.upload-dir}")
	String path;
	@Autowired
	private FoodRepository foodRepo;
	@Autowired
	private AuthenticatedUserFacade userFacade;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private NotificationService service;
	@Autowired
	private RolesRepository roleRepo;

	@Override
	public MessageResponses createFood(FoodDetailsRequest request) {
		return newFood(request);
	}

	@Override
	public MessageResponses updateFood(FoodDetailsRequest request) {
		return editFood(request);
	}

	@Override
	public FoodDetailsResponse getFood(long id) {
		return findFood(id);
	}

	@Cacheable
	@Override
	public List<FoodDetailsResponse> getAllFood(int page, int limit) {
		return findAllfoods(page, limit);
	}

	@Override
	public MessageResponses uploadFoodPicture(MultipartFile file, long id) {
		return setFoodPicture(file, id);
	}

	@Override
	public MessageResponses deleteFood(long id) {
		return removeFood(id);
	}

	private MessageResponses newFood(FoodDetailsRequest request) {
		UserEntity user = userFacade.getUser();
		Food food = foodRepo.findByName(request.getName());
		if (request.getId() != 0) {
			return new MessageResponses(MessageResponses.CODE_ERROR, ErrorMessages.SET_DEFAULT_ID.getErrorMessages());
		}
		if (food != null) {
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
		}
		FoodDTO transfer = new ModelMapper().map(request, FoodDTO.class);
		food = new ModelMapper().map(transfer, Food.class);
		food.setDateCreated(new Date());
		food.setCreatedBy(user.getId());
		Food newFood = foodRepo.save(food);
		if (newFood != null) {
			List<UserEntity> users = userRepo.findByEmailVerificationStatus(true);
			users.forEach(appUser -> {
				Roles role = roleRepo.findByName(FunctionUtils.ROLE_TYPE_USER);
				System.out.println("role name: " + role.getName());
				if (appUser.getRole().getName().equalsIgnoreCase(role.getName())) {
					service.createNotification(NotificationMessages.FOOD_MESSAGE, appUser, new Date(),
							NotificationMessages.TYPE_ALERT);
				}
			});
		}
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_CREATE);
	}

	private MessageResponses editFood(FoodDetailsRequest request) {
		UserEntity user = userFacade.getUser();
		if (foodRepo.findByName(request.getName()) != null) {
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
		}
		if (request.getId() > 0) {
			Food food = foodRepo.findById(request.getId()).get();
			if (food == null) {
				return new MessageResponses(MessageResponses.CODE_ERROR,
						ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
			}
			food.setAmount(request.getAmount());
			food.setDescription(request.getDescription());
			food.setName(request.getName());
			food.setDateEdited(new Date());
			food.setEditedBy(user.getId());
			Food newFood = foodRepo.save(food);
			if (newFood != null) {
				List<UserEntity> users = userRepo.findByEmailVerificationStatus(true);
				users.forEach(appUser -> {
					Roles role = roleRepo.findByName(FunctionUtils.ROLE_TYPE_USER);
					if (appUser.getRole().getName().equalsIgnoreCase(role.getName())) {
						service.createNotification(NotificationMessages.FOOD_MODIFIED_MESSAGE, appUser, new Date(),
								NotificationMessages.TYPE_ALERT);
					}
				});
			}
		}else {
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.SET_INPUT_ID.getErrorMessages());
		}
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPDATE);
	}

	private FoodDetailsResponse findFood(long id) {
		Food food = foodRepo.findById(id).get();
		if (food == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		FoodDetailsResponse returnFood = new ModelMapper().map(food, FoodDetailsResponse.class);
		return returnFood;
	}

	private MessageResponses setFoodPicture(MultipartFile file, long id) {
		Food food = foodRepo.findById(id).get();
		final String fileDataName = FunctionUtils.FOOD_IMAGE_PATH + food.getName() + file.getOriginalFilename();
		Path convertFile = Paths.get(path + fileDataName);
		try {
			Files.copy(file.getInputStream(), convertFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException iOException) {
			iOException.printStackTrace();
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.FILE_UPLOAD_FAILED.getErrorMessages());
		}
		food.setPhotoUrl(fileDataName);
		foodRepo.save(food);
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPLOAD);
	}

	private List<FoodDetailsResponse> findAllfoods(int page, int limit) {
		List<FoodDetailsResponse> returnFoods = new ArrayList<>();
		if (page > 0)
			page -= 1;
		Pageable request = PageRequest.of(page, limit);
		Page<Food> foodPage = foodRepo.allLatestFood(request);
		if (foodPage.isEmpty()) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		List<Food> allFoods = foodPage.getContent();
		allFoods.forEach(food -> {
			FoodDetailsResponse foodModel = new ModelMapper().map(food, FoodDetailsResponse.class);
			returnFoods.add(foodModel);
		});
		return returnFoods;
	}

	private MessageResponses removeFood(long id) {
		Food food = foodRepo.findById(id).get();
		if (food == null) {
			return new MessageResponses(MessageResponses.CODE_ERROR, ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		foodRepo.delete(food);
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_DELETE);
	}

}
