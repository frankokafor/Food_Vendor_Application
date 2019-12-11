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
import org.springframework.web.bind.annotation.RestController;

import com.frankokafor.food_vendor.request.objects.OrderRequest;
import com.frankokafor.food_vendor.services.OrderService;
import com.frankokafor.food_vendor.utilities.ResourceUrls;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {
	private final OrderService service;

	public OrderController(OrderService service) {
		super();
		this.service = service;
	}
	
	@ApiOperation(value = "creates new order...",notes = "make id 0 to create a new order.... end point to create a new order")
	@PostMapping(path = ResourceUrls.CREATE_ORDER, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity createOrder(@RequestBody OrderRequest request) {
		return new ResponseEntity<>(service.createOrder(request), HttpStatus.OK);
	}

	@ApiOperation(value = "delete order",notes = "append the order id to delete order")
	@DeleteMapping(path = ResourceUrls.DELETE_ORDER)
	public ResponseEntity deleteOrder(@PathVariable("id") long id) {
		return new ResponseEntity<>(service.deleteOrder(id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "update existing order",notes = "set order id to edit existing order....end point to update an existing order")
	@PutMapping(path = ResourceUrls.UPDATE_ORDER, consumes = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity updateUpdate(@RequestBody OrderRequest request) {
		return new ResponseEntity<>(service.updateOrder(request), HttpStatus.OK);
	}
	
	@ApiOperation(value = "get user orders",notes = "append the user id to get all orders made by user")
	@GetMapping(path = ResourceUrls.USER_ORDERS, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getUserOrders(@PathVariable("userId") long userId) {
		return new ResponseEntity<>(service.userOrders(userId), HttpStatus.OK);
	}
	
	@ApiOperation(value = "get all orders",notes = "get all orders in the database")
	@GetMapping(path = ResourceUrls.ALL_ORDERS, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getallOrders() {
		return new ResponseEntity<>(service.allOrders(), HttpStatus.OK);
	}
}
