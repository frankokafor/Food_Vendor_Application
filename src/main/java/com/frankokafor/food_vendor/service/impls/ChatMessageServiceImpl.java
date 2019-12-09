package com.frankokafor.food_vendor.service.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.models.ChatMessages;
import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.repositories.ChatMessageRepository;
import com.frankokafor.food_vendor.repositories.UserRepository;
import com.frankokafor.food_vendor.response.objects.ChatMessageResponse;
import com.frankokafor.food_vendor.security.AuthenticatedUserFacade;
import com.frankokafor.food_vendor.services.ChatMessageService;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

	private final AuthenticatedUserFacade userFacade;
	private final ChatMessageRepository chatRepo;
	private final UserRepository userRepo;

	public ChatMessageServiceImpl(AuthenticatedUserFacade userFacade, ChatMessageRepository chatRepo,
			UserRepository userRepo) {
		super();
		this.userFacade = userFacade;
		this.chatRepo = chatRepo;
		this.userRepo = userRepo;
	}

	@Override
	public ChatMessageResponse saveMessage(ChatMessageResponse response, SimpMessageHeaderAccessor headerAccessor) {
		return newMessage(response, headerAccessor);
	}

	@Override
	public List<ChatMessageResponse> allUserMessages() {
		return getAllUserMessages();
	}
	

	private ChatMessageResponse newMessage(ChatMessageResponse response, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", userFacade.getUser().getUsername());
		UserEntity sender = userRepo.findByUsername(response.getSender());
		ChatMessages chat = new ChatMessages(new Date(), sender, response.getContent());
		chatRepo.save(chat);
		return response;
	}
	
	private List<ChatMessageResponse> getAllUserMessages() {
		List<ChatMessageResponse> response = new ArrayList<>();
		List<ChatMessages> chats = chatRepo.findAll();
		chats.forEach(chat -> {
			ChatMessageResponse newResponse = new ChatMessageResponse();
			newResponse.setContent(chat.getMessage());
			newResponse.setSender(chat.getSender().getUsername());
			response.add(newResponse);
		});
		return response;
	}
}
