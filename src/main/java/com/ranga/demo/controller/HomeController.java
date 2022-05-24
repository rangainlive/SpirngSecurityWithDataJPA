package com.ranga.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ranga.demo.model.JWTRequest;
import com.ranga.demo.model.JWTResponse;
import com.ranga.demo.service.user.UserServiceImpl;
import com.ranga.demo.utility.JWTUtility;

@RestController
public class HomeController {
	
	@Autowired
	private JWTUtility jwtUtility;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@PostMapping("/home")
	public String home() {
		System.out.println("Home page Called");
		return "Welcome to the Home Page";
	}

	@PostMapping("/login")
	public JWTResponse authenticate(@RequestBody JWTRequest request) throws Exception {
		System.out.println("Login method Called"+request.getUsername() + " "+request.getPassword());
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getUsername(), 
							request.getPassword()
							)
					);
		} catch(BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		
		final UserDetails userDetails = userServiceImpl.loadUserByUsername(request.getUsername());
		
		final String token = jwtUtility.generateToken(userDetails);
		System.out.println(token);
		return new JWTResponse(token);
	}

}
