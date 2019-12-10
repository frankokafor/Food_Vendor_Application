package com.frankokafor.food_vendor.request.objects;

import lombok.Data;

@Data
public class PaymentRequestModel {

	private double total;
	private String currency;
	private String method;
	private String intent;
	private String description;
	private String cancleUri;
	private String successUri;
}
