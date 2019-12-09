package com.frankokafor.food_vendor.service.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.exceptions.UserServiceException;
import com.frankokafor.food_vendor.messages.ErrorMessages;
import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.models.Notifications;
import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.repositories.NotificationRepository;
import com.frankokafor.food_vendor.repositories.UserRepository;
import com.frankokafor.food_vendor.response.objects.NotificationResponse;
import com.frankokafor.food_vendor.services.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private NotificationRepository repo;

	@Async
	@Override
	public MessageResponses createNotification(String message, UserEntity user, Date created, String type) {
		return createNewNotification(message, user, created, type);
	}

	@Override
	public MessageResponses updateUserNotification(long notificationId) {
		return updateNotification(notificationId);
	}

	@Override
	public List<NotificationResponse> userNotifications(long userId) {
		return userMainNotifications(userId);
	}

	@Override
	public MessageResponses deleteNotification(long notificationId) {
		return deleteUserNotification(notificationId);
	}

	private MessageResponses deleteUserNotification(long notificationId) {
		try {
			Notifications notifications = repo.findById(notificationId).get();
			if (notifications == null) {
				return MessageResponses.response(MessageResponses.CODE_ERROR,
						ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
			}
			repo.delete(notifications);
			return MessageResponses.response(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPDATE);
		} catch (Exception e) {
			return MessageResponses.response(MessageResponses.CODE_ERROR, MessageResponses.MESSAGE_ERROR);
		}

	}

	private List<NotificationResponse> userMainNotifications(long userId) {
		List<NotificationResponse> returnValue = new ArrayList<>();
		UserEntity user = userRepo.findById(userId).get();
		List<Notifications> notifications = repo.allLatestNotification(user);
		if (notifications.isEmpty()) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		notifications.forEach(notification -> {
			NotificationResponse response = new ModelMapper().map(notification, NotificationResponse.class);
			returnValue.add(response);
		});
		return returnValue;
	}

	private MessageResponses createNewNotification(String message, UserEntity user, Date created, String type) {
		try {
			Notifications notifications = new Notifications(user, type, message, created);
			repo.save(notifications);
			return MessageResponses.response(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPDATE);
		} catch (Exception e) {
			return MessageResponses.response(MessageResponses.CODE_ERROR, MessageResponses.MESSAGE_ERROR);
		}

	}

	private MessageResponses updateNotification(long notificationId) {
		try {
			if (notificationId > 0) {
				Notifications notifications = repo.findById(notificationId).get();
				notifications.setReadStatus(true);
				repo.save(notifications);
			}else {
				return new MessageResponses(MessageResponses.CODE_ERROR,
						ErrorMessages.SET_INPUT_ID.getErrorMessages());
			}
			return MessageResponses.response(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPDATE);
		} catch (Exception e) {
			return MessageResponses.response(MessageResponses.CODE_ERROR, MessageResponses.MESSAGE_ERROR);
		}
	}

}
