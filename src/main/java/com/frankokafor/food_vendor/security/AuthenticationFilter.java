package com.frankokafor.food_vendor.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankokafor.food_vendor.configurations.SpringApplicationContext;
import com.frankokafor.food_vendor.models.Roles;
import com.frankokafor.food_vendor.models.UserEntity;
import com.frankokafor.food_vendor.repositories.UserRepository;
import com.frankokafor.food_vendor.request.objects.UserLoginRequest;
import com.frankokafor.food_vendor.utilities.TokenUtils;
import com.google.gson.Gson;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final TokenUtils utils = new TokenUtils();
	private final Gson gson = new Gson();
	private final Map m = new HashMap();

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserLoginRequest loginUser = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(),
					loginUser.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.ALL_VALUE);
			try {
				m.put("code", "error");
				m.put("message", "usuccessful, invalid username or password \n");
				String json = gson.toJson(m);
				PrintWriter pr = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				pr.write(json);
			} catch (IOException ex1) {
				return null;
			}
		}
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String userName = ((User) authResult.getPrincipal()).getUsername();
		String token = utils.generateAuthenticationToken(userName);
		UserRepository userRepo = (UserRepository) SpringApplicationContext.getBean("userRepository");
		UserEntity currentUser = userRepo.findByEmail(userName);

		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		response.addHeader("User ID", currentUser.getUserId());
		m.put("code", "success");
		m.put("message", "Login successful");
		m.put("token", SecurityConstants.TOKEN_PREFIX + token);
		Roles rs = currentUser.getRole();
		String role = rs.getName();
		m.put("role", role);
		String str = gson.toJson(m);
		PrintWriter pr = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		pr.write(str);
		currentUser.setLastLogin(new Date());
		userRepo.save(currentUser);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		super.unsuccessfulAuthentication(request, response, failed);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.ALL_VALUE);
		m.put("code", "error");
		m.put("message", "usuccessful, invalid username or password");
		String json = gson.toJson(m);
		PrintWriter pr = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		pr.write(json);
	}
}
