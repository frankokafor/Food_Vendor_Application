package com.frankokafor.food_vendor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.frankokafor.food_vendor.models.Order;
import com.frankokafor.food_vendor.models.UserEntity;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	@Query(value = "SELECT * FROM orders o WHERE o.user_id=:user ORDER BY o.date_created DESC",nativeQuery = true)
	List<Order> allLatestOrder(@Param("user") UserEntity user);
	
	Order findByName(String name);
	
	@Query(value = "SELECT * FROM orders o ORDER BY o.date_created DESC",nativeQuery = true)
	List<Order> allOrders();
}
