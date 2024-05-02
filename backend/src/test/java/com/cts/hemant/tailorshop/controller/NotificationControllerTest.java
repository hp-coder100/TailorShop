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

import com.cts.hemant.tailorshop.payload.NotificationDto;
import com.cts.hemant.tailorshop.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters=false)
class NotificationControllerTest {
	

	private final NotificationDto NOT_DTO_1 = new NotificationDto(1L, "Hello, How are You?", 1L, 1L);

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NotificationService notificationService;

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
	void testCreateNotification() throws Exception {
		when(notificationService.createNotification(NOT_DTO_1)).thenReturn(NOT_DTO_1);
		mockMvc.perform(post("/notification/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(NOT_DTO_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.notificationId").value(1L));
	}

	
	@Test
	void testFindAllNotifications_withShopIdAndCustomerId() throws Exception {
		when(notificationService.findAllByCustomerIdAndShopId(1L, 1L)).thenReturn(Arrays.asList(NOT_DTO_1));
		mockMvc.perform(get("/notification/findAll?shopId=1&customerId=1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));
	}

	

	
	@Test
	void testUpdateNotification() throws Exception{
		when(notificationService.updateNotification(NOT_DTO_1)).thenReturn(NOT_DTO_1);
		mockMvc.perform(put("/notification/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(NOT_DTO_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.notificationId").value(1L));
	}

	@Test
	void testFindByNotificationId() throws Exception{
		when(notificationService.findByNotificationId(1L)).thenReturn(NOT_DTO_1);
		mockMvc.perform(get("/notification/find/{notificationId}", 1).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.notificationId").value(1));

	}

	@Test
	void testDeleteNotification() throws Exception{
		
		doNothing().when(notificationService).deleteNotification(1L);
		mockMvc.perform(delete("/notification/delete/{notificationId}", 1).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").value("Notification deleted Successfully"));

	}

}
