package com.frankokafor.food_vendor.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.frankokafor.food_vendor.models.Food;

@Repository
public interface FoodRepository extends PagingAndSortingRepository<Food,Long>{
	Food findByName(String name);
	
	@Query(value = "SELECT * FROM food f ORDER BY f.date_created DESC",nativeQuery = true)
	Page<Food> allLatestFood(Pageable page);
}
