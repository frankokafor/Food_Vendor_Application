package com.frankokafor.food_vendor.messages;

import java.util.Date;

import lombok.Data;

@Data
public class CustomErrorMessage {
	private Date timestamp;
	private String message;

	public CustomErrorMessage() {
		super();
	}

	public CustomErrorMessage(Date timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}
}
