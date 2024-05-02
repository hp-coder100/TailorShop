package com.cts.hemant.tailorshop.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
		long userId;
		String role;
		String token;
}
