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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.frankokafor.food_vendor.exceptions.UserServiceException;
import com.frankokafor.food_vendor.messages.ErrorMessages;
import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.messages.NotificationMessages;
import com.frankokafor.food_vendor.models.PasswordReset;
import com.frankokafor.food_vendor.models.Roles;
import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.operation.OperationStatusModel;
import com.frankokafor.food_vendor.operation.RequestOperationName;
import com.frankokafor.food_vendor.operation.RequestOperationStatus;
import com.frankokafor.food_vendor.repositories.PasswordResetRepository;
import com.frankokafor.food_vendor.repositories.RolesRepository;
import com.frankokafor.food_vendor.repositories.UserRepository;
import com.frankokafor.food_vendor.request.objects.PasswordResetModel;
import com.frankokafor.food_vendor.request.objects.PasswordResetRequest;
import com.frankokafor.food_vendor.request.objects.UserDetailsRequest;
import com.frankokafor.food_vendor.request.objects.UserIdRequest;
import com.frankokafor.food_vendor.request.objects.UserUpdateRequest;
import com.frankokafor.food_vendor.response.objects.UserDetailsResponse;
import com.frankokafor.food_vendor.security.AuthenticatedUserFacade;
import com.frankokafor.food_vendor.services.NotificationService;
import com.frankokafor.food_vendor.services.UserService;
import com.frankokafor.food_vendor.utilities.FunctionUtils;
import com.frankokafor.food_vendor.utilities.TokenUtils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private FunctionUtils funUtils;
	@Autowired
	private TokenUtils utils;
	@Autowired
	private RolesRepository roleRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private EmailServiceImpl service;
	@Autowired
	private PasswordResetRepository passRepo;
	@Autowired
	private AuthenticatedUserFacade userFacade;
	@Value("${file.upload-dir}")
	String path;
	@Autowired
	private NotificationService notifService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		return loadNewUserByUsername(username);
	}

	@Override
	public MessageResponses createUser(UserDetailsRequest request) {
		return createNewUser(request);
	}

	@Override
	public UserDetailsResponse getUserByUserId(String userId) {
		return getUser(userId);
	}

	@CachePut(value = "users", key = "#request.id")
	@Override
	public MessageResponses updateUser(UserUpdateRequest request) {
		return editUser(request);
	}

	@CacheEvict(value = "users", key = "#userId")
	@Override
	public MessageResponses deleteUser(String userId) {
		return removeUser(userId);
	}

	@Cacheable(value = "users")
	@Override
	public List<UserDetailsResponse> getAllUsers(int page, int limit) {
		return findAllUsers(page, limit);
	}

	@Cacheable(value = "users")
	@Override
	public List<UserDetailsResponse> getAllVerifiedUsers() {
		return findAllVerifiedUsers();
	}

	@Override
	public MessageResponses updatePassword(String password) {
		return editPassword(password);
	}

	@CachePut(value = "users")
	@Override
	public MessageResponses makeAdmin(UserIdRequest request) {
		return makeUserAdmin(request);
	}

	@Override
	public OperationStatusModel verifyEmailToken(String token) {
		OperationStatusModel model = new OperationStatusModel();
		model.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		model.setOperationResult(RequestOperationStatus.ERROR.name());
		if (emailTokenVerification(token)) {
			model.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
		return model;
	}

	@Override
	public OperationStatusModel requestPasswordResetToken(PasswordResetRequest requestModel) {
		OperationStatusModel model = new OperationStatusModel();
		model.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET_TOKEN.name());
		model.setOperationResult(RequestOperationStatus.ERROR.name());
		if (requestForPasswordResetToken(requestModel)) {
			model.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
		return model;
	}

	@Override
	public OperationStatusModel passwordReset(PasswordResetModel requestModel) {
		OperationStatusModel model = new OperationStatusModel();
		model.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		model.setOperationResult(RequestOperationStatus.ERROR.name());
		if (resetPassword(requestModel)) {
			model.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
		return model;
	}

	@Override
	public MessageResponses uploadProfilePicture(MultipartFile file) {
		return uploadUserProfilePicture(file);
	}

	private UserDetails loadNewUserByUsername(String username) {
		UserEntity entity = userRepo.findByEmail(username);
		if (entity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		return new User(entity.getEmail(), entity.getEncryptedPassword(), entity.getEmailVerificationStatus(), true,
				true, true, new ArrayList<>());
	}

	private Boolean emailTokenVerification(String token) {
		boolean returnValue = false;
		UserEntity user = userRepo.findByEmailVerificationToken(token);
		if (user != null) {
			boolean isTokenExpired = utils.hasTokenExpired(token);
			if (!isTokenExpired) {
				user.setEmailVerificationToken(null);
				user.setEmailVerificationStatus(true);
				UserEntity newUser = userRepo.save(user);
				if (newUser != null) {
					List<UserEntity> users = userRepo.findByEmailVerificationStatus(true);
					users.forEach(appUser -> {
						Roles role = roleRepo.findByName(FunctionUtils.ROLE_TYPE_ADMIN);
						if (appUser.getRole().getName().equalsIgnoreCase(role.getName())) {
							notifService.createNotification(NotificationMessages.NEW_USER, appUser, new Date(),
									NotificationMessages.TYPE_ALERT);
						}
					});
				}
				returnValue = true;
			}
		}
		return returnValue;
	}

	private Boolean requestForPasswordResetToken(PasswordResetRequest requestModel) {
		Boolean value = false;
		UserEntity user = userRepo.findByEmail(requestModel.getEmail());
		if (user == null) {
			return value;
		}
		String token = utils.generatePasswordResetToken(user.getUserId());
		PasswordReset password = new PasswordReset();
		password.setToken(token);
		password.setUserDetails(user);
		passRepo.save(password);
		value = service.sendPasswordEmail(user.getFirstName(), user.getEmail(), token);
		return value;
	}

	private Boolean resetPassword(PasswordResetModel requestModel) {
		Boolean value = false;
		if (utils.hasTokenExpired(requestModel.getToken())) {
			return value;
		}
		PasswordReset newPassword = passRepo.findByToken(requestModel.getToken());
		if (newPassword == null) {
			return value;
		}
		String encodedPassword = passwordEncoder.encode(requestModel.getPassword());
		UserEntity user = newPassword.getUserDetails();
		user.setEncryptedPassword(encodedPassword);
		UserEntity returnUser = userRepo.save(user);
		if (returnUser != null && returnUser.getEncryptedPassword().equals(encodedPassword)) {
			value = true;
		}
		passRepo.delete(newPassword);
		return value;
	}

	private MessageResponses uploadUserProfilePicture(MultipartFile file) {
		final String fileDataName = FunctionUtils.PROFILE_IMAGE_PATH + userFacade.getUser().getLastName()
				+ file.getOriginalFilename();
		Path convertFile = Paths.get(path + fileDataName);
		try {
			Files.copy(file.getInputStream(), convertFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException iOException) {
			iOException.printStackTrace();
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.FILE_UPLOAD_FAILED.getErrorMessages());
		}
		UserEntity user = userFacade.getUser();
		user.setPhotoUrl(fileDataName);
		userRepo.save(user);
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPLOAD);
	}

	private List<UserDetailsResponse> findAllUsers(int page, int limit) {
		List<UserDetailsResponse> returnUsers = new ArrayList<>();
		if (page > 0)
			page -= 1;
		Pageable request = PageRequest.of(page, limit);
		Page<UserEntity> usersPage = userRepo.findAll(request);
		if (usersPage.isEmpty()) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		List<UserEntity> allUsers = usersPage.getContent();
		allUsers.forEach(user -> {
			UserDetailsResponse userModel = new ModelMapper().map(user, UserDetailsResponse.class);
			returnUsers.add(userModel);
		});
		return returnUsers;
	}

	private List<UserDetailsResponse> findAllVerifiedUsers() {
		List<UserDetailsResponse> returnUsers = new ArrayList<>();
		List<UserEntity> users = userRepo.findByEmailVerificationStatus(true);
		if (users.isEmpty()) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		users.forEach(user -> {
			UserDetailsResponse userModel = new ModelMapper().map(user, UserDetailsResponse.class);
			returnUsers.add(userModel);
		});
		return returnUsers;
	}

	private MessageResponses removeUser(String userId) {
		UserEntity userEntity = userRepo.findByUserId(userId);
		if (userEntity == null) {
			return new MessageResponses(MessageResponses.CODE_ERROR, ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		userRepo.delete(userEntity);
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_DELETE);
	}

	private MessageResponses editUser(UserUpdateRequest request) {
		if (request.getId() > 0) {
			UserEntity user = userFacade.getUser();
			user.setEmail(request.getEmail());
			user.setPhoneNumber(request.getPhoneNumber());
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setAddress(request.getAddress());
			user.setDateEdited(new Date());
			user.setEditedBy(user.getId());
			userRepo.save(user);
		}else {
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.SET_INPUT_ID.getErrorMessages());
		}
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPDATE);
	}

	private UserDetailsResponse getUser(String userId) {
		UserDetailsResponse user = new UserDetailsResponse();
		UserEntity userEntity = userRepo.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		user = new ModelMapper().map(userEntity, UserDetailsResponse.class);
		return user;
	}

	private MessageResponses createNewUser(UserDetailsRequest request) {
		String publicUserId = funUtils.generatedKey(10);
		if (request.getUsername().isEmpty() || request.getUsername() == null) {
			return new MessageResponses(MessageResponses.CODE_ERROR, ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessages());
		}
		if (userRepo.findByEmail(request.getEmail()) != null
				|| userRepo.findByUsername(request.getUsername()) != null) {
			return new MessageResponses(MessageResponses.CODE_ERROR,
					ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
		}
		UserEntity entity = new ModelMapper().map(request, UserEntity.class);
		Roles role = roleRepo.findByName(FunctionUtils.ROLE_TYPE_USER);
		entity.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
		entity.setUserId(publicUserId);
		entity.setRole(role);
		entity.setDateCreated(new Date());
		entity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
		UserEntity storedUser = userRepo.save(entity);
		service.sendText(storedUser);
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_CREATE);
	}

	private MessageResponses editPassword(String password) {
		UserEntity user = userFacade.getUser();
		String encodedPassword = passwordEncoder.encode(password);
		user.setEncryptedPassword(encodedPassword);
		userRepo.save(user);
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPDATE);
	}

	private MessageResponses makeUserAdmin(UserIdRequest request) {
		request.getUserIds().forEach(id -> {
			UserEntity user = userRepo.findById(id).get();
			if (user == null) {
				throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
			}
			Roles role = roleRepo.findByName(FunctionUtils.ROLE_TYPE_ADMIN);
			if (user.getRole().getName().equalsIgnoreCase(role.getName())) {
				throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
			}
			user.setRole(role);
			user.setUsername("ADMIN");
			userRepo.save(user);
		});
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPDATE);
	}
}
