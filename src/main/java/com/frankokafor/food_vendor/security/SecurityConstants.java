package com.frankokafor.food_vendor.security;

import com.frankokafor.food_vendor.configurations.SpringApplicationContext;
import com.frankokafor.food_vendor.utilities.AppProperties;

public class SecurityConstants {
	public static final long EMAIL_TOKEN_EXPIRATION_TIME = 1000*60*60*24;// 1 day
	public static final String TOKEN_PREFIX = "Bearer: ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/createUser";
	public static final String EMAIL_VERIFICATION_URL = "/users/email-verification";
	public static final String PASSWORD_RESET_TOKEN_URL = "/users/password-reset-token";
	public static final String PASSWORD_RESET_URL = "/users/reset-password";
	public static final long PASSWORD_TOKEN_EXPIRATION_TIME = 10800000;// 3 hours
	public static final String H2_CONSOLE = "/h2-console/**";
	public static final long LOGIN_TOKEN_EXPIRATION_TIME = 1000*60*60*20;// 20 hours

	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
}
