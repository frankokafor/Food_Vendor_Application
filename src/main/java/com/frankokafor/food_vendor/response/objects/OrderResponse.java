package com.frankokafor.food_vendor.response.objects;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OrderResponse {

	private long id;
	private String name;
	private double totalAmount;
	private String location;
	private List<FoodDetailsResponse> foods;
	private List<DrinkDetailsResponse> drinks;
	private Date dateCreated;
	private Date dateEdited;
	private String orderedBy;
	private boolean paymentStatus;
	private String paymentType;
}
