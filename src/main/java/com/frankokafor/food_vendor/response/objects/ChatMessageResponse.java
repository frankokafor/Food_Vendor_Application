package com.frankokafor.food_vendor.response.objects;

import lombok.Data;

@Data
public class ChatMessageResponse {
	private String type;

	private String content;

	private String sender;
}
