package com.frankokafor.food_vendor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.frankokafor.food_vendor.models.PasswordReset;
import com.frankokafor.food_vendor.models.UserEntity;


@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
	PasswordReset findByToken(String token);

	void deleteByUserDetails(UserEntity user);

}
