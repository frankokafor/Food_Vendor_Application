package com.frankokafor.food_vendor.request.objects;

import lombok.Data;

@Data
public class UserLoginRequest {

	private String email;
	private String password;

}
