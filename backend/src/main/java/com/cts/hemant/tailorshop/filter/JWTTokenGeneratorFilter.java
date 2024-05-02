package com.cts.hemant.tailorshop.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cts.hemant.tailorshop.util.SecurityConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (null != authentication) {

			String jwt = JWTService.generateToken(authentication.getName(), authentication.getAuthorities());
			response.setHeader(SecurityConstants.JWT_HEADER, jwt);

		}

		filterChain.doFilter(request, response);
	}

}
