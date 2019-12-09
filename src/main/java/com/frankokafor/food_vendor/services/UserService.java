package com.frankokafor.food_vendor.services;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.operation.OperationStatusModel;
import com.frankokafor.food_vendor.request.objects.PasswordResetModel;
import com.frankokafor.food_vendor.request.objects.PasswordResetRequest;
import com.frankokafor.food_vendor.request.objects.UserDetailsRequest;
import com.frankokafor.food_vendor.request.objects.UserIdRequest;
import com.frankokafor.food_vendor.request.objects.UserUpdateRequest;
import com.frankokafor.food_vendor.response.objects.UserDetailsResponse;

@Service
public interface UserService extends UserDetailsService {
	
	MessageResponses createUser(UserDetailsRequest request);

	UserDetailsResponse getUserByUserId(String userId);

	MessageResponses updateUser(UserUpdateRequest request);

	MessageResponses deleteUser(String userId);

	List<UserDetailsResponse> getAllUsers(int page, int limit);
	
	List<UserDetailsResponse> getAllVerifiedUsers();

	OperationStatusModel verifyEmailToken(String token);

	OperationStatusModel requestPasswordResetToken(PasswordResetRequest requestModel);

	OperationStatusModel passwordReset(PasswordResetModel requestModel);
	
	MessageResponses uploadProfilePicture(MultipartFile file);
	
	MessageResponses updatePassword(String password);
	
	MessageResponses makeAdmin(UserIdRequest request);
}
