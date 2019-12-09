package com.frankokafor.food_vendor.services;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.models.UserEntity;


@Service
public interface EmailService {
	
	void sendText(UserEntity user) throws MessagingException;

	Boolean sendPasswordEmail(String name, String email, String token) throws MessagingException;

}
