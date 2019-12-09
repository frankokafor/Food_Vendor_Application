package com.frankokafor.food_vendor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.repositories.UserRepository;

/**
 *
 * @author mac
 */
@Service
public class AuthenticatedUserImpl implements AuthenticatedUserFacade {

	@Autowired
	private UserRepository userRepo;

	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public String getName() {
		return this.getAuthentication().getName();
	}

	@Override
	public UserEntity getUser() {
		return this.getName() != null ? userRepo.findByEmail(this.getName()) : null;
	}
}
