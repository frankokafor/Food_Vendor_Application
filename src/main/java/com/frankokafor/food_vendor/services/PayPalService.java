package com.frankokafor.food_vendor.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.request.objects.ComfirmPaymentModel;
import com.frankokafor.food_vendor.request.objects.PaymentRequestModel;

@Service
public interface PayPalService {

	public Map<String, Object> createPayment(PaymentRequestModel model);
	public Map<String, Object> executePayment(ComfirmPaymentModel model);
}
