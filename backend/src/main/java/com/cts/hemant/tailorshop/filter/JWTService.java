package com.cts.hemant.tailorshop.filter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.payload.LoginDto;
import com.cts.hemant.tailorshop.payload.LoginResponse;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.util.SecurityConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TailorRepository tailorRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	protected static String generateToken(String name, Collection<? extends GrantedAuthority> authorities) {
		String jwt = Jwts.builder().issuer("TailorShop").subject("JWT Token").claim("username", name)
				.claim("authorities", populateAuthorities(authorities)).issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + 24 * 60 * 60 * 1000)).signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8))).compact();
		return jwt;
	}

	private static String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
		for (GrantedAuthority authority : collection) {
			authoritiesSet.add(authority.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}
    

	public LoginResponse loginUser(LoginDto loginDto) {
		System.out.println(loginDto);
		String username = loginDto.getUsername();
		String pwd = loginDto.getPassword();
		List<GrantedAuthority> authorities;
		Optional<Customer> customer = customerRepository.findByEmail(username);
		authorities = new ArrayList<>();

		if (customer.isPresent()) {
			if (passwordEncoder.matches(pwd, customer.get().getPassword())) {
				authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
				String tokenString = generateToken(username, authorities);
				LoginResponse loginResponse = new LoginResponse(customer.get().getCustomerId(),"ROLE_USER", tokenString);
				return loginResponse;

			} else {
				throw new BadCredentialsException("Invalid password!");
			}
		} else {
			Optional<Tailor> tailor = tailorRepository.findByEmail(username);
			if (tailor.isPresent()) {
				if (passwordEncoder.matches(pwd, tailor.get().getPassword())) {
					authorities.add(new SimpleGrantedAuthority("ROLE_TAILOR"));

					String tokenString = generateToken(username, authorities);
					LoginResponse loginResponse = new LoginResponse(tailor.get().getShopId(),"ROLE_TAILOR", tokenString);
					
					return loginResponse;
				} else {
					throw new BadCredentialsException("Invalid password!");
				}
			} else {
				throw new BadCredentialsException("No user registered with this details!");
			}
		}

	}

}
