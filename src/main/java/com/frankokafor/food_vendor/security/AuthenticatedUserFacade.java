/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frankokafor.food_vendor.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.models.UserEntity;

/**
 *
 * @author Barima
 */
@Service
public interface AuthenticatedUserFacade {

    Authentication getAuthentication();

    String getName();

    UserEntity getUser();
}
