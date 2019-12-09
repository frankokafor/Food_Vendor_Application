package com.frankokafor.food_vendor.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.request.objects.OrderRequest;
import com.frankokafor.food_vendor.response.objects.OrderResponse;

@Service
public interface OrderService {

	Map<String, Object> createOrder(OrderRequest request);
	MessageResponses deleteOrder(long id);
	Map<String, Object> updateOrder(OrderRequest request);
	List<OrderResponse> userOrders(long userId);
	List<OrderResponse> allOrders();
}
