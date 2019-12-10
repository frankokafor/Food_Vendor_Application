package com.frankokafor.food_vendor.request.objects;

import java.util.List;

import lombok.Data;

@Data
public class UserIdRequest {
	private List<Long> userIds;
}
