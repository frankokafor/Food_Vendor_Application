package com.frankokafor.food_vendor.service.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.messages.ErrorMessages;
import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.messages.NotificationMessages;
import com.frankokafor.food_vendor.models.Drinks;
import com.frankokafor.food_vendor.models.Food;
import com.frankokafor.food_vendor.models.Order;
import com.frankokafor.food_vendor.models.Roles;
import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.repositories.DrinksRepository;
import com.frankokafor.food_vendor.repositories.FoodRepository;
import com.frankokafor.food_vendor.repositories.OrderRepository;
import com.frankokafor.food_vendor.repositories.RolesRepository;
import com.frankokafor.food_vendor.repositories.UserRepository;
import com.frankokafor.food_vendor.request.objects.OrderRequest;
import com.frankokafor.food_vendor.response.objects.OrderResponse;
import com.frankokafor.food_vendor.security.AuthenticatedUserFacade;
import com.frankokafor.food_vendor.services.NotificationService;
import com.frankokafor.food_vendor.services.OrderService;
import com.frankokafor.food_vendor.utilities.FunctionUtils;
import com.frankokafor.food_vendor.response.objects.DrinkDetailsResponse;
import com.frankokafor.food_vendor.response.objects.FoodDetailsResponse;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RolesRepository roleRepo;
	@Autowired
	private NotificationService service;
	@Autowired
	private DrinksRepository drinkRepo;
	@Autowired
	private FoodRepository foodRepo;
	@Autowired
	private AuthenticatedUserFacade userFacade;
	@Autowired
	private FunctionUtils utils;

	@Override
	public Map<String, Object> createOrder(OrderRequest request) {
		return createNewOrder(request);
	}

	@Override
	public MessageResponses deleteOrder(long id) {
		return removeOrder(id);
	}

	@Override
	public Map<String, Object> updateOrder(OrderRequest request) {
		return editOrder(request);
	}

	@Override
	public List<OrderResponse> userOrders(long userId) {
		return allUserOrders(userId);
	}
	
	@Override
	public List<OrderResponse> allOrders() {
		return allDatabaseOrders();
	}

	private List<OrderResponse> allUserOrders(long userId) {
		UserEntity user = userRepo.findById(userId).get();
		List<Order> orders = orderRepo.allLatestOrder(user);
		List<OrderResponse> orderResponse = new ArrayList<>();
		orders.forEach(order -> {
			List<DrinkDetailsResponse> drinkResponse = new ArrayList<>();
			List<Drinks> drinks = order.getDrinkList();
			drinks.forEach(drink -> {
				DrinkDetailsResponse dr = new ModelMapper().map(drink, DrinkDetailsResponse.class);
				drinkResponse.add(dr);
			});
			List<FoodDetailsResponse> foodResponse = new ArrayList<>();
			List<Food> foods = order.getFoodList();
			foods.forEach(food -> {
				FoodDetailsResponse fd = new ModelMapper().map(food, FoodDetailsResponse.class);
				foodResponse.add(fd);
			});
			OrderResponse response = new ModelMapper().map(order, OrderResponse.class);
			response.setDrinks(drinkResponse);
			response.setFoods(foodResponse);
			orderResponse.add(response);
		});
		return orderResponse;
	}

	private MessageResponses removeOrder(long id) {
		Order order = orderRepo.getOne(id);
		if (order == null) {
			return new MessageResponses(MessageResponses.CODE_ERROR, ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		orderRepo.delete(order);
		return new MessageResponses(MessageResponses.CODE_OK, MessageResponses.MESSAGE_DELETE);
	}

	private Map<String, Object> createNewOrder(OrderRequest request) {
		Map<String, Object> response = new HashMap<>();
		double addedAmount = 0;
		List<Double> total = new ArrayList<>();
		Order order1 = orderRepo.findByName(request.getName());
		if (request.getOrderId() != 0) {
			response.put(MessageResponses.CODE_ERROR, ErrorMessages.SET_DEFAULT_ID.getErrorMessages());
			return response;
		}
		if (order1 != null) {
			response.put(MessageResponses.CODE_ERROR, ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
			response.put(MessageResponses.MESSAGE, "Order with name "+request.getName()+" already exist");
			return response;
		}
		List<Food> foods = new ArrayList<>();
		List<Drinks> drinks = new ArrayList<>();
		request.getDrinkId().forEach(i -> {
			Drinks drink = drinkRepo.findById(i).get();
			drinks.add(drink);
			total.add(drink.getAmount());
		});
		request.getFoodId().forEach(i -> {
			Food food = foodRepo.findById(i).get();
			foods.add(food);
			total.add(food.getAmount());
		});
		for (double t : total) {
			addedAmount += t;
		}
		Order order = new ModelMapper().map(request, Order.class);
		double finalAmount = priceType(request.getPaymentType(), addedAmount);
		order.setTotalAmount(finalAmount);
		order.setDrinkList(drinks);
		order.setFoodList(foods);
		order.setDateCreated(new Date());
		order.setUserDetails(userFacade.getUser());
		order.setOrderedBy(userFacade.getUser().getUsername());
		order.setCreatedBy(userFacade.getUser().getId());
		Order newOrder = orderRepo.save(order);
		if (newOrder != null) {
			List<UserEntity> users = userRepo.findByEmailVerificationStatus(true);
			users.forEach(appUser -> {
				Roles role = roleRepo.findByName(FunctionUtils.ROLE_TYPE_ADMIN);
				if (appUser.getRole().getName().equalsIgnoreCase(role.getName())) {
					service.createNotification(NotificationMessages.ORDER_MESSAGE, appUser, new Date(),
							NotificationMessages.TYPE_ALERT);
				}
			});
		}
		response.put(MessageResponses.CODE_OK, MessageResponses.MESSAGE_CREATE);
		response.put("Order", order(newOrder));
		return response;
	}

	private Map<String, Object> editOrder(OrderRequest request) {
		Map<String, Object> response = new HashMap<>();
		double addedAmount = 0;
		List<Double> total = new ArrayList<>();
		if (orderRepo.findByName(request.getName()) != null) {
			response.put(MessageResponses.CODE_ERROR, ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessages());
			response.put(MessageResponses.MESSAGE, "Order with name "+request.getName()+" already exist");
			return response;
		}
		if (request.getOrderId() > 0) {
			Order order = orderRepo.findById(request.getOrderId()).get();
			List<Food> foods = new ArrayList<>();
			List<Drinks> drinks = new ArrayList<>();
			request.getDrinkId().forEach(i -> {
				Drinks drink = drinkRepo.findById(i).get();
				drinks.add(drink);
				total.add(drink.getAmount());
			});
			request.getFoodId().forEach(i -> {
				Food food = foodRepo.findById(i).get();
				foods.add(food);
				total.add(food.getAmount());
			});
			for (double t : total) {
				addedAmount += t;
			}
			double finalAmount = priceType(request.getPaymentType(), addedAmount);
			order.setLocation(request.getLocation());
			order.setName(request.getName());
			order.setTotalAmount(finalAmount);
			order.setDrinkList(drinks);
			order.setFoodList(foods);
			order.setDateEdited(new Date());
			order.setPaymentType(request.getPaymentType());
			order.setEditedBy(userFacade.getUser().getId());
			Order newOrder = orderRepo.save(order);
			if (newOrder != null) {
				List<UserEntity> users = userRepo.findByEmailVerificationStatus(true);
				users.forEach(appUser -> {
					Roles role = roleRepo.findByName(FunctionUtils.ROLE_TYPE_ADMIN);
					if (appUser.getRole().getName().equalsIgnoreCase(role.getName())) {
						service.createNotification(NotificationMessages.ORDER_MODIFIED_MESSAGE, appUser, new Date(),
								NotificationMessages.TYPE_ALERT);
					}
				});
			}
			response.put(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPDATE);
			response.put("Order", order(newOrder));
		} else {
			response.put(MessageResponses.CODE_ERROR, ErrorMessages.SET_INPUT_ID.getErrorMessages());
			return response;
		}
		return response;
	}

	private double priceType(String paymentType, double totalPrice) {
		if (paymentType.equalsIgnoreCase(FunctionUtils.PAY_ON_DELIVERY)) {
			return utils.payOnDelivery(totalPrice);
		} else
			return utils.payWithCard(totalPrice);
	}

	private OrderResponse order(Order order) {
		List<DrinkDetailsResponse> drinkResponse = new ArrayList<>();
		List<Drinks> drinks = order.getDrinkList();
		drinks.forEach(drink -> {
			DrinkDetailsResponse dr = new ModelMapper().map(drink, DrinkDetailsResponse.class);
			drinkResponse.add(dr);
		});
		List<FoodDetailsResponse> foodResponse = new ArrayList<>();
		List<Food> foods = order.getFoodList();
		foods.forEach(food -> {
			FoodDetailsResponse fd = new ModelMapper().map(food, FoodDetailsResponse.class);
			foodResponse.add(fd);
		});
		OrderResponse response = new ModelMapper().map(order, OrderResponse.class);
		response.setDrinks(drinkResponse);
		response.setFoods(foodResponse);
		return response;
	}

	private List<OrderResponse> allDatabaseOrders() {
		List<Order> orders = orderRepo.findAll();
		List<OrderResponse> orderResponse = new ArrayList<>();
		orders.forEach(order -> {
			List<DrinkDetailsResponse> drinkResponse = new ArrayList<>();
			List<Drinks> drinks = order.getDrinkList();
			drinks.forEach(drink -> {
				DrinkDetailsResponse dr = new ModelMapper().map(drink, DrinkDetailsResponse.class);
				drinkResponse.add(dr);
			});
			List<FoodDetailsResponse> foodResponse = new ArrayList<>();
			List<Food> foods = order.getFoodList();
			foods.forEach(food -> {
				FoodDetailsResponse fd = new ModelMapper().map(food, FoodDetailsResponse.class);
				foodResponse.add(fd);
			});
			OrderResponse response = new ModelMapper().map(order, OrderResponse.class);
			response.setDrinks(drinkResponse);
			response.setFoods(foodResponse);
			orderResponse.add(response);
		});
		return orderResponse;
	}
	

}
