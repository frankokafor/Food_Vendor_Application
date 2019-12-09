package com.frankokafor.food_vendor.configurations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PaypalConfiguration {
	@Value("${paypal.client.id}")
	private String clientId;
	@Value("${paypal.client.secret}")
	private String clientSecret;
	@Value("${paypal.mode}")
	private String mode;
	
	@Bean
	public Map<String, String> payPalSdkConfig(){
		Map<String, String> config = new HashMap<>();
		config.put("mode", mode);
		return config;
	}
	
	@Bean
	public APIContext apiContext() throws PayPalRESTException {
		APIContext context = new APIContext(clientId, clientSecret, mode, payPalSdkConfig());
		return context;
	}
}
