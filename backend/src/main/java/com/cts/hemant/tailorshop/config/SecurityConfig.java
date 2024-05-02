package com.cts.hemant.tailorshop.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.cts.hemant.tailorshop.filter.CsrfCookieFilter;
import com.cts.hemant.tailorshop.filter.JWTService;
import com.cts.hemant.tailorshop.filter.JWTTokenGeneratorFilter;
import com.cts.hemant.tailorshop.filter.JWTTokenValidatorFilter;
import com.cts.hemant.tailorshop.filter.LogAthentication;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfig {

	private static final String[] ANY_AUTH = { "/appointment/create", "/appointment/update", "/appointment/find/*",
			"/category/findAll", "/category/find/**", "/tailor/findAll", "/payment/findAll", "/payment/find/**",
			"notification/findAll", "notification/find/**","/payment/create","/tailor/find/**","/customer/find/**"
			
	};

	private static final String[] CUSTOMER_AUTH_STRINGS = { "/appointment/findAll/customer/**", "/customer/update",
			  "payment/findAll/customer/**" };

	private static final String[] TAILOR_AUTH_STRINGS = { "/appointment/findAll/tailor/**", "/category/update",
			"/category/create",  "/tailor/update", "/payment/update", "payment/findAll/tailor/**",
			"notification/create", "notification/update", "measurement/create", "measurement/update",
			"measurement/find/**", "measurement/findAll/**", 
			"category/delete/**"
			};

	private static final String[] SWAGGER_AUTH_WHITELIST = {
			"/loginUser",

			"/customer/create",
			"/tailor/create",
			// -- Swagger UI v2
			"/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui.html", "/webjars/**",
			// -- Swagger UI v3 (OpenAPI)
			"/v3/api-docs/**", "/swagger-ui/**"
			// other public endpoints of your API may be appended to this array
	};

	@Bean
	SecurityFilterChain defaSecurityFilterChain2(HttpSecurity http) throws Exception {
		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
		requestHandler.setCsrfRequestAttributeName("_csrf");
		
		
		
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setExposedHeaders(Arrays.asList("Authorization"));
						config.setMaxAge(3600L);
						return config;
					}
				})).csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers("**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
				
				.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new JWTTokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(new LogAthentication(), UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(new JWTTokenGeneratorFilter(), LogAthentication.class)
				.authorizeHttpRequests((request) ->

				request.requestMatchers(CUSTOMER_AUTH_STRINGS).hasRole("CUSTOMER")
						.requestMatchers(TAILOR_AUTH_STRINGS)
						.hasRole("TAILOR").requestMatchers(ANY_AUTH).authenticated()
						.requestMatchers(SWAGGER_AUTH_WHITELIST).permitAll())
				
				.formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());
		return http.build();

	}
	
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
	@Bean 
	public JWTService jwtService() {
		return new JWTService();
	}
	

}
