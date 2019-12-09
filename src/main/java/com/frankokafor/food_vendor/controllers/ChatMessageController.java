package com.frankokafor.food_vendor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frankokafor.food_vendor.response.objects.ChatMessageResponse;
import com.frankokafor.food_vendor.services.ChatMessageService;
import com.frankokafor.food_vendor.utilities.ResourceUrls;

import io.swagger.annotations.ApiOperation;

@RestController
public class ChatMessageController {
	private final ChatMessageService service;

	public ChatMessageController(ChatMessageService service) {
		this.service = service;
	}

	@MessageMapping(ResourceUrls.MESSAGE_MAPPING)
	@SendTo(ResourceUrls.CHAT_URL)
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity message(@Payload ChatMessageResponse response, SimpMessageHeaderAccessor headerAccessor) {
		return new ResponseEntity<>(service.saveMessage(response, headerAccessor),HttpStatus.OK);
	}
	
	@ApiOperation(value = "get all message in database")
	@GetMapping(path = ResourceUrls.ALL_MESSAGES, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getAllMessage() {
		return new ResponseEntity<>(service.allUserMessages(), HttpStatus.OK);
	}
}
