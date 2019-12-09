package com.frankokafor.food_vendor.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.response.objects.NotificationResponse;

@Service
public interface NotificationService {

	MessageResponses createNotification(String message, UserEntity user, Date created, String type);
	MessageResponses updateUserNotification(long id);
	List<NotificationResponse> userNotifications(long userId);
	MessageResponses deleteNotification(long id);
}
