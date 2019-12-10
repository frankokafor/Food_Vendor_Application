package com.frankokafor.food_vendor.request.objects;

import lombok.Data;

@Data
public class UserDetailsRequest {
	private String email;
	private String password;
	private String phoneNumber;
	private String address;
	private String lastName;
	private String firstName;
	private String username;
}
