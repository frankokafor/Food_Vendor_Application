package com.frankokafor.food_vendor.messages;

import org.springframework.stereotype.Component;

@Component
public class NotificationMessages {
	public static final String TYPE_ALERT = "Update";
	public static final String FOOD_MESSAGE = "New Food Available";
	public static final String DRINK_MESSAGE = "New Drink Available";
	public static final String ORDER_MESSAGE = "New Order Made";
	public static final String ORDER_MODIFIED_MESSAGE = "New Modified Order Made";
	public static final String FOOD_MODIFIED_MESSAGE = "New Modified Food Available";
	public static final String DRINK_MODIFIED_MESSAGE = "New Modified Drink Available";
	public static final String NEW_USER = "New Verified User Joined";
}
