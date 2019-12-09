package com.frankokafor.food_vendor.utilities;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class FunctionUtils {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

	public static String PROFILE_IMAGE_PATH = "profile_image/";
	public static String FILE_UPLOAD_PATH = "uploads/";
	public static String DRINK_IMAGE_PATH = "drink_image/";
	public static String FOOD_IMAGE_PATH = "food_image/";
	public static String ROLE_TYPE_ADMIN = "ADMIN";
	public static String ROLE_TYPE_USER = "USER";
	public static String PAY_ON_DELIVERY = "PAY ON DELIVERY";

	public String generatedKey(int key) {
		return generatedRandomString(key);
	}

	private String generatedRandomString(int key) {
		StringBuilder main = new StringBuilder(key);

		for (int i = 0; i < key; i++) {
			main.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(main);
	}
	
	public Double payOnDelivery(double total) {
		double distance = 20*10;
		double totalAmount = total + distance;
		return totalAmount;
	}
	
	public Double payWithCard(double total) {
		double distance = 20*10;
		double totalAmount = total + distance;
		double discount = totalAmount - (2.5/100*totalAmount);
		return discount;
	}
}
