package com.frankokafor.food_vendor.request.objects;

import lombok.Data;

@Data
public class UserUpdateRequest {
	private long id;
	private String email;
	private String phoneNumber;
	private String address;
	private String lastName;
	private String firstName;
}
