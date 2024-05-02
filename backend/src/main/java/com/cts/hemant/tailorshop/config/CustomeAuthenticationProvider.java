package com.cts.hemant.tailorshop.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;

@Component
public class CustomeAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TailorRepository tailorRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		String username = authentication.getName();
		String pwd = authentication.getCredentials().toString();
		
		List<GrantedAuthority> authorities;
		Optional<Customer> customer = customerRepository.findByEmail(username);
		authorities = new ArrayList<>();

		if (customer.isPresent()) {
			if (passwordEncoder.matches(pwd, customer.get().getPassword())) {
				authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
				return new UsernamePasswordAuthenticationToken(username, pwd, authorities);

			} else {
				throw new BadCredentialsException("Invalid password!");
			}
		} else {
			Optional<Tailor> tailor = tailorRepository.findByEmail(username);
			if (tailor.isPresent()) {
				if (passwordEncoder.matches(pwd, tailor.get().getPassword())) {
					authorities.add(new SimpleGrantedAuthority("ROLE_TAILOR"));
					return new UsernamePasswordAuthenticationToken(username, pwd, authorities);

				} else {
					throw new BadCredentialsException("Invalid password!");
				}
			} else {
				throw new BadCredentialsException("No user registered with this details!");
			}
		}

	}


	 @Override
	    public boolean supports(Class<?> authentication) {
	        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	    }


}
