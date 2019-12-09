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

import com.frankokafor.food_vendor.request.objects.DrinkDetailsRequest;
import com.frankokafor.food_vendor.services.DrinkService;
import com.frankokafor.food_vendor.utilities.ResourceUrls;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/drink")
public class DrinkController {

	private final DrinkService service;

	public DrinkController(DrinkService service) {
		super();
		this.service = service;
	}

	@ApiOperation(value = "set id to 0 to creates new drink...",notes = "end point to create a new drink")
	@PostMapping(path = ResourceUrls.CREATE_DRINK, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity createDrink(@RequestBody DrinkDetailsRequest request) {
		return new ResponseEntity<>(service.createDrink(request), HttpStatus.OK);
	}
	
	@ApiOperation(value = "set drink id to update existing drink",notes = "end point to update an existing drink")
	@PutMapping(path = ResourceUrls.UPDATE_DRINK, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity updateDrink(@RequestBody DrinkDetailsRequest request) {
		return new ResponseEntity<>(service.updateDrink(request), HttpStatus.OK);
	}
	
	@ApiOperation(value = "get drink",notes = "append the drink id to get drink")
	@GetMapping(path = ResourceUrls.GET_DRINK, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getDrink(@PathVariable("id") long id) {
		return new ResponseEntity<>(service.getDrink(id), HttpStatus.OK);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "integer", paramType = "header"),
		@ApiImplicitParam(name = "limit", value = "integer", paramType = "header")
	})
	@ApiOperation(value = "get all drinks in database",notes = "append pagination with limit not more than 25 drinks per page")
	@GetMapping(path = ResourceUrls.ALL_DRINKS, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getAllDrinks(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		return new ResponseEntity<>(service.getAllDrinks(page, limit), HttpStatus.OK);
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "file", value = "multipart", paramType = "header")
	})
	@ApiOperation(value = "upload drink picture",notes = "set file param to upload picture")
	@PostMapping(path = ResourceUrls.UPLOAD_DRINK_PICTURE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity uploadDrinkPicture(@RequestParam("file") MultipartFile file, @PathVariable("id") long id) {
		return new ResponseEntity<>(service.uploadDrinkPicture(file, id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "delete drink",notes = "append the drink id to delete")
	@DeleteMapping(path = ResourceUrls.DELETE_DRINK)
	public ResponseEntity deleteDrink(@PathVariable("id") long id) {
		return new ResponseEntity<>(service.deleteDrink(id), HttpStatus.OK);
	}
}
