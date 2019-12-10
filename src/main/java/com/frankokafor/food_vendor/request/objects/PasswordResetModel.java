package com.frankokafor.food_vendor.request.objects;

import lombok.Data;

@Data
public class PasswordResetModel {

	private String token;
	private String password;

}
