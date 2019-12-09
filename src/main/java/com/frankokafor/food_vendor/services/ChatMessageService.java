package com.frankokafor.food_vendor.services;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.response.objects.ChatMessageResponse;

@Service
public interface ChatMessageService {

	ChatMessageResponse saveMessage(ChatMessageResponse response,SimpMessageHeaderAccessor headerAccessor);
	List<ChatMessageResponse> allUserMessages();
	
}
