package com.frankokafor.food_vendor.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.request.objects.FoodDetailsRequest;
import com.frankokafor.food_vendor.response.objects.FoodDetailsResponse;

@Service
public interface FoodService {

	MessageResponses createFood(FoodDetailsRequest request);
	MessageResponses updateFood(FoodDetailsRequest request);
	FoodDetailsResponse getFood(long id);
	List<FoodDetailsResponse> getAllFood(int page, int limit);
	MessageResponses uploadFoodPicture(MultipartFile file, long id);
	MessageResponses deleteFood(long id);
}
