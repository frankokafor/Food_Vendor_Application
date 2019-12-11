package com.frankokafor.food_vendor.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.frankokafor.food_vendor.exceptions.UserServiceException;
import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.repositories.PasswordResetRepository;
import com.frankokafor.food_vendor.repositories.RolesRepository;
import com.frankokafor.food_vendor.repositories.UserRepository;
import com.frankokafor.food_vendor.response.objects.UserDetailsResponse;
import com.frankokafor.food_vendor.security.AuthenticatedUserFacade;
import com.frankokafor.food_vendor.service.impls.EmailServiceImpl;
import com.frankokafor.food_vendor.service.impls.UserServiceImpl;
import com.frankokafor.food_vendor.services.NotificationService;
import com.frankokafor.food_vendor.utilities.FunctionUtils;
import com.frankokafor.food_vendor.utilities.TokenUtils;

class UserServiceImplTest {
	@InjectMocks
	UserServiceImpl service;
	@Mock 
	private UserRepository userRepo;
	@Mock 
	private FunctionUtils funUtils;
	@Mock 
	private TokenUtils utils;
	@Mock 
	private RolesRepository roleRepo;
	@Mock 
	private BCryptPasswordEncoder passwordEncoder;
	@Mock 
	private EmailServiceImpl eService;
	@Mock 
	private PasswordResetRepository passRepo;
	@Mock 
	private AuthenticatedUserFacade userFacade;
	@Mock 
	private NotificationService notifService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetUserByUserId() {
		UserEntity user = new UserEntity();
		user.setFirstName("frank");
		when(userRepo.findByUserId(anyString())).thenReturn(user);
		UserDetailsResponse udr = service.getUserByUserId("ebukaas");
		assertEquals(udr.getFirstName(), user.getFirstName());
		}

	@Test
	void testGetAllVerifiedUsers() {
		when(userRepo.findByUserId(anyString())).thenReturn(null);
		assertThrows(UserServiceException.class, () -> {
			service.getUserByUserId("ebukas");
		});
	}

}
