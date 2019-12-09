package com.frankokafor.food_vendor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.frankokafor.food_vendor.models.Notifications;
import com.frankokafor.food_vendor.models.UserEntity;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long>{

	List<Notifications> findByUserDetails(UserEntity user);
	
	@Query(value = "SELECT * FROM notifications n WHERE n.user_id=:user ORDER BY n.date_created DESC",nativeQuery = true)
	List<Notifications> allLatestNotification(@Param("user") UserEntity user);
}
