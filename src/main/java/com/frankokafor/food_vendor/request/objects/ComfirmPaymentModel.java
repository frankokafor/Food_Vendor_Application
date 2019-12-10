package com.frankokafor.food_vendor.request.objects;

import lombok.Data;

@Data
public class ComfirmPaymentModel {
	private String paymentId;
	private long orderId;
}
