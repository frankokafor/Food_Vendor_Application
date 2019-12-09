package com.frankokafor.food_vendor.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.frankokafor.food_vendor.models.Drinks;

@Repository
public interface DrinksRepository extends PagingAndSortingRepository<Drinks, Long> {

	Drinks findByName(String name);
	
	@Query(value = "SELECT * FROM drinks d ORDER BY d.date_created DESC",nativeQuery = true)
	Page<Drinks> allLatestDrinks(Pageable page);
}
