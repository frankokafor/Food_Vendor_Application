package com.frankokafor.food_vendor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frankokafor.food_vendor.request.objects.ComfirmPaymentModel;
import com.frankokafor.food_vendor.request.objects.PaymentRequestModel;
import com.frankokafor.food_vendor.services.PayPalService;
import com.frankokafor.food_vendor.utilities.ResourceUrls;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/paypal")
public class PayPalController {
	private final PayPalService service;

	public PayPalController(PayPalService service) {
		super();
		this.service = service;
	}
	
	@ApiOperation(value = "make paypal payment...",notes = "end point to make payment..send payment body to process payment")
	@PostMapping(path = ResourceUrls.MAKE_PAYMENT, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity makePayment(@RequestBody PaymentRequestModel model) {
		return new ResponseEntity<>(service.createPayment(model),HttpStatus.OK);
	}
	
	@ApiOperation(value = "confirm paypal payment...",notes = "put the order id to complete payment.....end point to complete payment")
	@PostMapping(path = ResourceUrls.COMPLETE_PAYMENT, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity completePayment(@RequestBody ComfirmPaymentModel model) {
		return new ResponseEntity<>(service.executePayment(model),HttpStatus.OK);
	}

}
