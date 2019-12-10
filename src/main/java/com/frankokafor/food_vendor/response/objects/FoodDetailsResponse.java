package com.frankokafor.food_vendor.response.objects;

import java.util.Date;

import lombok.Data;

@Data
public class FoodDetailsResponse {

	private long id;
	private String name;
	private String photoUrl;
	private String description;
	private double amount;
	private Date dateCreated;
	private Date dateEdited;
}
