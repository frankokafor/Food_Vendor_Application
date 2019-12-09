package com.frankokafor.food_vendor.response.objects;

import java.util.Date;
import java.util.List;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<FoodDetailsResponse> getFoods() {
		return foods;
	}

	public void setFoods(List<FoodDetailsResponse> foods) {
		this.foods = foods;
	}

	public List<DrinkDetailsResponse> getDrinks() {
		return drinks;
	}

	public void setDrinks(List<DrinkDetailsResponse> drinks) {
		this.drinks = drinks;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateEdited() {
		return dateEdited;
	}

	public void setDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public boolean isPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

}
