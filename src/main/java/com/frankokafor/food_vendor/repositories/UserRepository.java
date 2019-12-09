package com.frankokafor.food_vendor.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.frankokafor.food_vendor.models.UserEntity;
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);

	UserEntity findByUserId(String userId);
	
	UserEntity findByUsername(String username);

	UserEntity findByEmailVerificationToken(String token);
	
	List<UserEntity> findByEmailVerificationStatus(Boolean status);

}
