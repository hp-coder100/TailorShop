package com.cts.hemant.tailorshop.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.hemant.tailorshop.payload.CustomerDto;
import com.cts.hemant.tailorshop.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters=false)
class CutsomerControllerTest {
	
	private final CustomerDto CUSTOMER_DTO_1 = new CustomerDto(1L, "Hemant", "Prajapati", "Guna, MadhyaPradesh",
			123456789, "Hemant@gmail.com", "123456");
	
	@Autowired
	private MockMvc mockMvc;
	

	@MockBean
	private CustomerService customerService;

	private static ObjectMapper objectMapper;

	public String mapToJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}
	
	@BeforeAll
	public static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	
	@Test
	void testCreateCustomer() throws  Exception {
		when(customerService.createCustomer(CUSTOMER_DTO_1)).thenReturn(CUSTOMER_DTO_1);
		mockMvc.perform(post("/customer/create").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(CUSTOMER_DTO_1)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.customerId").value(1L));
				
	}

	@Test
	void testFindCustomers() throws Exception {
		when(customerService.findAllCustomers()).thenReturn(Arrays.asList(CUSTOMER_DTO_1));
		mockMvc.perform(get("/customer/findAll").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(1));

	}
	
//	@Test
//	void testFindCustomers_withEmailId() throws Exception {
//		when(customerService.findByEmailId("Hemant@gmail.com")).thenReturn(CUSTOMER_DTO_1);
//		mockMvc.perform(get("/customer?email=Hemant@gmail.com").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//		.andExpect(jsonPath("$.customerId").value(1L));
//		
//	}

	@Test
	void testUpdateCustomer() throws  Exception {
		when(customerService.updateCustomer(CUSTOMER_DTO_1)).thenReturn(CUSTOMER_DTO_1);
		mockMvc.perform(put("/customer/update").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(CUSTOMER_DTO_1)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.customerId").value(1L));
				
	}

	@Test
	void testDeleteCustomer() throws Exception {
		doNothing().when(customerService).deleteCustomer(1L);

		mockMvc.perform(delete("/customer/delete/{customerId}", 1L).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").value("Customer deleted Successfully"));

	}

	@Test
	void testFindCustomerById() throws Exception {
		when(customerService.findByCustomerId(1L)).thenReturn(CUSTOMER_DTO_1);
		mockMvc.perform(get("/customer/find/{customerId}", 1).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.customerId").value(1L));

	}

}
