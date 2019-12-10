package com.frankokafor.food_vendor.request.objects;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {

	private long orderId;
	private String name;
	private String location;
	private List<Long> foodId;
	private List<Long> drinkId;
	private String paymentType;
}
