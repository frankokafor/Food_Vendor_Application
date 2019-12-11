package com.frankokafor.food_vendor.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.frankokafor.food_vendor.request.objects.PasswordResetModel;
import com.frankokafor.food_vendor.request.objects.PasswordResetRequest;
import com.frankokafor.food_vendor.request.objects.UserDetailsRequest;
import com.frankokafor.food_vendor.request.objects.UserIdRequest;
import com.frankokafor.food_vendor.request.objects.UserUpdateRequest;
import com.frankokafor.food_vendor.services.UserService;
import com.frankokafor.food_vendor.utilities.ResourceUrls;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/users")
public class UserController {
	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}
	
	@ApiOperation(value = "creates new user...",notes = "end point to create a new user")
	@PostMapping(path = ResourceUrls.CREATE_USER, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity createUser(@RequestBody UserDetailsRequest request) {
		return new ResponseEntity<>(service.createUser(request), HttpStatus.OK);
	}
	
	@ApiOperation(value = "get user",notes = "append the user public userid to get user")
	@GetMapping(path = ResourceUrls.GET_USER, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getUser(@PathVariable("userId") String userId) {
		return new ResponseEntity<>(service.getUserByUserId(userId), HttpStatus.OK);
	}
	
	@ApiOperation(value = "set user id to update existing user",notes = "end point to update an existing user")
	@PutMapping(path = ResourceUrls.UPDATE_USER, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity updateUser(@RequestBody UserUpdateRequest request) {
		return new ResponseEntity<>(service.updateUser(request), HttpStatus.OK);
	}
	
	@ApiOperation(value = "delete existing user",notes = "append the user public userid to delete user")
	@DeleteMapping(path = ResourceUrls.DELETE_USER)
	public ResponseEntity deleteUser(@PathVariable("userId") String userId) {
		return new ResponseEntity<>(service.deleteUser(userId), HttpStatus.OK);
	}
	
	@ApiOperation(value = "get all verified users",notes = "end point to get users with verified email address")
	@GetMapping(path = ResourceUrls.ALL_VERIFIED_USERS, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getAllVerifiedUsers() {
		return new ResponseEntity<>(service.getAllVerifiedUsers(), HttpStatus.OK);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "integer", paramType = "header"),
		@ApiImplicitParam(name = "limit", value = "integer", paramType = "header")
	})
	@ApiOperation(value = "get all users in database",notes = "append pagination with limit not more than 25 users per page")
	@GetMapping(path = ResourceUrls.ALL_USERS, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		return new ResponseEntity<>(service.getAllUsers(page, limit), HttpStatus.OK);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "Bearer", paramType = "header")
	})
	@ApiOperation(value = "verify email address",notes = "append email token to verify user email address")
	@PostMapping(path = ResourceUrls.VERIFY_EMAIL)
	public ResponseEntity verifyEmail(@RequestParam("token") String token) {
		return new ResponseEntity<>(service.verifyEmailToken(token), HttpStatus.OK);
	}
	
	@ApiOperation(value = "get password reset token",notes = "end point to get token for password reset")
	@PostMapping(path = ResourceUrls.PASSWORD_RESET_TOKEN, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity requestPasswordResetToken(@RequestBody PasswordResetRequest requestModel) {
		return new ResponseEntity<>(service.requestPasswordResetToken(requestModel), HttpStatus.OK);
	}
	
	@ApiOperation(value = "reset password",notes = "reset forget password.include the password reset token and the new passeord")
	@PostMapping(path = ResourceUrls.PASSWORD_RESET, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity passwordReset(@RequestBody PasswordResetModel requestModel) {
		return new ResponseEntity<>(service.passwordReset(requestModel), HttpStatus.OK);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "file", value = "multipart", paramType = "header")
	})
	@ApiOperation(value = "upload profile picture",notes = "set file param to upload picture")
	@PostMapping(path = ResourceUrls.UPLOAD_PROFILE_PICTURE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity uploadProfilePicture(@RequestParam("file") MultipartFile file) {
		return new ResponseEntity<>(service.uploadProfilePicture(file), HttpStatus.OK);
	}
	
	@ApiOperation(value = "make a user an admin",notes = "send list of userids to make users an admin")
	@PostMapping(path = ResourceUrls.MAKE_USER_ADMIN, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity makeAdmin(@RequestBody UserIdRequest request) {
		return new ResponseEntity<>(service.makeAdmin(request), HttpStatus.ACCEPTED);
	}
	
	@ApiOperation(value = "update logged in user password",notes = "send password to update")
	@PostMapping(path = ResourceUrls.UPDATE_LOGGED_IN_USER_PASSWORD, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity updatePassword(@RequestBody String password) {
		return new ResponseEntity<>(service.updatePassword(password), HttpStatus.OK);
	}

}
