package com.frankokafor.food_vendor.response.objects;

import java.util.Date;

import lombok.Data;

@Data
public class NotificationResponse {
	private long id;
	private Date dateCreated;
	private String message;
	private Boolean readStatus;
	private String type;

}
