package com.frankokafor.food_vendor.request.objects;

import lombok.Data;

@Data
public class FoodDetailsRequest {
	private long id;
	private String name;
	private String description;
	private double amount;
}
