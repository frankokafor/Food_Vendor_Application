package com.frankokafor.food_vendor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.frankokafor.food_vendor.models.ChatMessages;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessages, Long>{

}
