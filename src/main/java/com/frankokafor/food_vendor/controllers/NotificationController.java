package com.frankokafor.food_vendor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frankokafor.food_vendor.services.NotificationService;
import com.frankokafor.food_vendor.utilities.ResourceUrls;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
	private final NotificationService service;

	public NotificationController(NotificationService service) {
		super();
		this.service = service;
	}
	
	@ApiOperation(value = "get all notifications", notes = "set the userid to get all notifications of the user")
	@GetMapping(path = ResourceUrls.USER_NOTIFICATIONS, produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity userNotification(@PathVariable("userId") long userId) {
		return new ResponseEntity<>(service.userNotifications(userId), HttpStatus.OK);
	}
	
	@ApiOperation(value = "delete existing notification", notes = "set the notification id to delete")
	@DeleteMapping(path = ResourceUrls.DELETE_NOTIFICATION)
	public ResponseEntity deletenotification(@PathVariable("id") long id) {
		return new ResponseEntity<>(service.deleteNotification(id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "get notification read staus", notes = "set the notification id to update notification")
	@PutMapping(path = ResourceUrls.UPDATE_NOTIFICATION)
	public ResponseEntity getnotification(@PathVariable("id") long id) {
		return new ResponseEntity<>(service.updateUserNotification(id), HttpStatus.OK);
	}

}
