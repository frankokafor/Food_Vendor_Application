package com.frankokafor.food_vendor.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.request.objects.DrinkDetailsRequest;
import com.frankokafor.food_vendor.response.objects.DrinkDetailsResponse;

@Service
public interface DrinkService {

	MessageResponses createDrink(DrinkDetailsRequest request);

	MessageResponses updateDrink(DrinkDetailsRequest request);

	DrinkDetailsResponse getDrink(long id);

	List<DrinkDetailsResponse> getAllDrinks(int page, int limit);

	MessageResponses uploadDrinkPicture(MultipartFile file, long id);

	MessageResponses deleteDrink(long id);
}
