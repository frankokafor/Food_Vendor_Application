package com.frankokafor.food_vendor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.frankokafor.food_vendor.models.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long>{
	
	Roles findByName(String name);

}
