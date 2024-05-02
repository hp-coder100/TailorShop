package com.cts.hemant.tailorshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.hemant.tailorshop.filter.JWTService;
import com.cts.hemant.tailorshop.payload.LoginDto;
import com.cts.hemant.tailorshop.payload.LoginResponse;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;

@RestController
public class AuthController {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	TailorRepository tailorRepository;
	
	@Autowired
	JWTService jwtService;

	@PostMapping("/loginUser")
	public ResponseEntity<Object> getUserDetailsAfterLogin(@RequestBody LoginDto loginDto) {
		LoginResponse loginResponse  = jwtService.loginUser(loginDto);
		
		return new ResponseEntity<Object>(loginResponse , HttpStatus.OK);
	}
}
