package com.frankokafor.food_vendor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.frankokafor.food_vendor.request.objects.FoodDetailsRequest;
import com.frankokafor.food_vendor.services.FoodService;
import com.frankokafor.food_vendor.utilities.ResourceUrls;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/food")
public class FoodController {
	private final FoodService service;

	public FoodController(FoodService service) {
		super();
		this.service = service;
	}
	
	@ApiOperation(value = "set id to 0 to create new food...",notes = "end point to create a new food")
	@PostMapping(path = ResourceUrls.CREATE_FOOD, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity createFood(@RequestBody FoodDetailsRequest request) {
		return new ResponseEntity<>(service.createFood(request), HttpStatus.OK);
	}
	
	@ApiOperation(value = "set food id to update existing food",notes = "end point to update an existing food")
	@PutMapping(path = ResourceUrls.UPDATE_FOOD, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity updateFood(@RequestBody FoodDetailsRequest request) {
		return new ResponseEntity<>(service.updateFood(request), HttpStatus.OK);
	}
	
	@ApiOperation(value = "get food",notes = "append the food id to get food")
	@GetMapping(path = ResourceUrls.GET_FOOD, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getFood(@PathVariable("id") long id) {
		return new ResponseEntity<>(service.getFood(id), HttpStatus.OK);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "integer", paramType = "header"),
		@ApiImplicitParam(name = "limit", value = "integer", paramType = "header")
	})
	@ApiOperation(value = "get all food in database",notes = "append pagination with limit not more than 25 foods per page")
	@GetMapping(path = ResourceUrls.ALL_FOODS, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getAllFoods(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		return new ResponseEntity<>(service.getAllFood(page, limit), HttpStatus.OK);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "file", value = "multipart", paramType = "header")
	})
	@ApiOperation(value = "upload food picture",notes = "set file param to upload picture")
	@PostMapping(path = ResourceUrls.UPLOAD_FOOD_PICTURE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity uploadFoodPicture(@RequestParam("file") MultipartFile file, @PathVariable("id") long id) {
		return new ResponseEntity<>(service.uploadFoodPicture(file, id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "delete food",notes = "append the food id to delete")
	@DeleteMapping(path = ResourceUrls.DELETE_FOOD)
	public ResponseEntity deleteFood(@PathVariable("id") long id) {
		return new ResponseEntity<>(service.deleteFood(id), HttpStatus.OK);
	}

}
