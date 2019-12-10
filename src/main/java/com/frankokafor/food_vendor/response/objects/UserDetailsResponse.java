package com.frankokafor.food_vendor.response.objects;

import java.util.Date;

import lombok.Data;
@Data
public class UserDetailsResponse {
	private long Id;
	private String address;
	private Date lastLogin;
	private Date dateCreated;
	private String phoneNumber;
	private String userId;
	private String photoUrl;
	private String firstName;
	private String lastName;
	private String username;
}
