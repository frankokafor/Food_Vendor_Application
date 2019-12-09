package com.frankokafor.food_vendor.request.objects;

import java.util.List;

public class OrderRequest {

	private long orderId;
	private String name;
	private String location;
	private List<Long> foodId;
	private List<Long> drinkId;
	private String paymentType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Long> getFoodId() {
		return foodId;
	}

	public void setFoodId(List<Long> foodId) {
		this.foodId = foodId;
	}

	public List<Long> getDrinkId() {
		return drinkId;
	}

	public void setDrinkId(List<Long> drinkId) {
		this.drinkId = drinkId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

}
