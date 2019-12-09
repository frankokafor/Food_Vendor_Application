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
import com.frankokafor.food_vendor.models.Drinks;
import com.frankokafor.food_vendor.models.Roles;
import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.repositories.DrinksRepository;
import com.frankokafor.food_vendor.repositories.RolesRepository;
import com.frankokafor.food_vendor.repositories.UserRepository;
import com.frankokafor.food_vendor.request.objects.DrinkDetailsRequest;
import com.frankokafor.food_vendor.response.objects.DrinkDetailsResponse;
import com.frankokafor.food_vendor.security.AuthenticatedUserFacade;
import com.frankokafor.food_vendor.services.DrinkService;
import com.frankokafor.food_vendor.services.NotificationService;
import com.frankokafor.food_vendor.transferObjects.DrinkDTO;
import com.frankokafor.food_vendor.utilities.FunctionUtils;

@Service
public class DrinkServiceImpl implements DrinkService {

	@Value("${file.upload-dir}")
	String path;
	@Autowired
	private DrinksRepository drinkRepo;
	@Autowired
	private AuthenticatedUserFacade userFacade;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RolesRepository roleRepo;
	@Autowired
	private NotificationService service;

	@Override
	public MessageResponses createDrink(DrinkDetailsRequest request) {
		return newDrink(request);
	}

	@Override
	public MessageResponses updateDrink(DrinkDetailsRequest request) {
		return editDrink(request);
	}

	@Override
	public DrinkDetailsResponse getDrink(long id) {
		return findDrink(id);
	}

	@Cacheable
	@Override
	public List<DrinkDetailsResponse> getAllDrinks(int page, int limit) {
		return findAllDrinks(page, limit);
	}

	@Override
	public MessageResponses uploadDrinkPicture(MultipartFile file, long id) {
		return setDrinkPicture(file, id);
	}

	@Override
	public MessageResponses deleteDrink(long id) {
		return removeDrink(id);
	}

	private MessageResponses newDrink(DrinkDetailsRequest request) {
		UserEntity user = userFacade.getUser();
		Drinks drink = drinkRepo.findByName(request.getName());
		if (request.getId() != 0) {
			return new MessageResponses(MessageResponses.CODE_ERROR, ErrorMessages.SET_DEFAULT_ID.getErrorMessages());
		}
		if (drink != null) {
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
		}
		DrinkDTO transfer = new ModelMapper().map(request, DrinkDTO.class);
		drink = new ModelMapper().map(transfer, Drinks.class);
		drink.setDateCreated(new Date());
		drink.setCreatedBy(user.getId());
		Drinks newDrink = drinkRepo.save(drink);
		if (newDrink != null) {
			List<UserEntity> users = userRepo.findByEmailVerificationStatus(true);
			users.forEach(appUser -> {
				Roles role = roleRepo.findByName(FunctionUtils.ROLE_TYPE_USER);
				if (appUser.getRole().getName().equalsIgnoreCase(role.getName())) {
					service.createNotification(NotificationMessages.DRINK_MESSAGE, appUser, new Date(),
							NotificationMessages.TYPE_ALERT);
				}
			});
		}
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_CREATE);
	}

	private MessageResponses editDrink(DrinkDetailsRequest request) {
		UserEntity user = userFacade.getUser();
		if (drinkRepo.findByName(request.getName()) != null) {
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
		}
		if (request.getId() > 0) {
			Drinks drink = drinkRepo.findById(request.getId()).get();
			if (drink == null) {
				return new MessageResponses(MessageResponses.CODE_ERROR,
						ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
			}
			drink.setAmount(request.getAmount());
			drink.setDescription(request.getDescription());
			drink.setName(request.getName());
			drink.setDateEdited(new Date());
			drink.setEditedBy(user.getId());
			Drinks newDrink = drinkRepo.save(drink);
			if (newDrink != null) {
				List<UserEntity> users = userRepo.findByEmailVerificationStatus(true);
				users.forEach(appUser -> {
					Roles role = roleRepo.findByName(FunctionUtils.ROLE_TYPE_USER);
					if (appUser.getRole().getName().equalsIgnoreCase(role.getName())) {
						service.createNotification(NotificationMessages.DRINK_MODIFIED_MESSAGE, appUser, new Date(),
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

	private DrinkDetailsResponse findDrink(long id) {
		Drinks drink = drinkRepo.findById(id).get();
		if (drink == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		DrinkDetailsResponse returnDrink = new ModelMapper().map(drink, DrinkDetailsResponse.class);
		return returnDrink;
	}

	private MessageResponses setDrinkPicture(MultipartFile file, long id) {
		Drinks drink = drinkRepo.findById(id).get();
		final String fileDataName = FunctionUtils.FOOD_IMAGE_PATH + drink.getName() + file.getOriginalFilename();
		Path convertFile = Paths.get(path + fileDataName);
		try {
			Files.copy(file.getInputStream(), convertFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException iOException) {
			iOException.printStackTrace();
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.FILE_UPLOAD_FAILED.getErrorMessages());
		}
		drink.setPhotoUrl(fileDataName);
		drinkRepo.save(drink);
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPLOAD);
	}

	private List<DrinkDetailsResponse> findAllDrinks(int page, int limit) {
		List<DrinkDetailsResponse> returnDrinks = new ArrayList<>();
		if (page > 0)
			page -= 1;
		Pageable request = PageRequest.of(page, limit);
		Page<Drinks> drinkPage = drinkRepo.allLatestDrinks(request);
		if (drinkPage.isEmpty()) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		List<Drinks> allDrinks = drinkPage.getContent();
		allDrinks.forEach(drink -> {
			DrinkDetailsResponse drinkModel = new ModelMapper().map(drink, DrinkDetailsResponse.class);
			returnDrinks.add(drinkModel);
		});
		return returnDrinks;
	}

	private MessageResponses removeDrink(long id) {
		Drinks drink = drinkRepo.findById(id).get();
		if (drink == null) {
			return new MessageResponses(MessageResponses.CODE_ERROR, ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		drinkRepo.delete(drink);
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_DELETE);
	}
}
