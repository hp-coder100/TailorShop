package com.cts.hemant.tailorshop.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.hemant.tailorshop.payload.PaymentDto;
import com.cts.hemant.tailorshop.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters=false)
class PaymentControllerTest {
	private final PaymentDto PAY_DTO_1 = new PaymentDto(1L, 1000.0, LocalDate.parse("2023-02-23"), "Pending", 1L);

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PaymentService paymentService;

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
	void testCreatePayment() throws  Exception {
		when(paymentService.createPayment(PAY_DTO_1)).thenReturn(PAY_DTO_1);
		mockMvc.perform(post("/payment/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(PAY_DTO_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.paymentId").value(1L));
	}

	@Test
	void testFindAllPayments() throws  Exception {
		when(paymentService.findAllPayments()).thenReturn(Arrays.asList(PAY_DTO_1));
		mockMvc.perform(get("/payment/findAll").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));	}

	@Test
	void testFindAllPayments_withAppointmentId() throws  Exception {
		when(paymentService.findAllByAppointmentId(1L)).thenReturn(Arrays.asList(PAY_DTO_1));
		mockMvc.perform(get("/payment/findAll?appointmentId=1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));	}

	@Test
	void testFindAllPayments_withStatus() throws  Exception {
		when(paymentService.findAllByStatus("Pending")).thenReturn(Arrays.asList(PAY_DTO_1));
		mockMvc.perform(get("/payment/findAll?status=Pending").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));	}

	@Test
	void testFindAllPayments_withDate() throws  Exception {
		when(paymentService.findAllByDate(LocalDate.parse("2023-02-23"))).thenReturn(Arrays.asList(PAY_DTO_1));
		mockMvc.perform(get("/payment/findAll?date=2023-02-23").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));	}

	
	
	
	@Test
	void testUpdatePayment()throws  Exception {
		when(paymentService.updatePayment(PAY_DTO_1)).thenReturn(PAY_DTO_1);
		mockMvc.perform(put("/payment/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(PAY_DTO_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.paymentId").value(1L));
	}

	@Test
	void testFindByNotificationId() throws  Exception{
		when(paymentService.findByPaymentId(1L)).thenReturn(PAY_DTO_1);
		mockMvc.perform(get("/payment/find/{paymentId}", 1L).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.paymentId").value(1));

	}

	@Test
	void testDeleteNotification() throws  Exception{
		doNothing().when(paymentService).deletePayment(1L);

		mockMvc.perform(delete("/payment/delete/{paymentId}", 1L).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").value("Payment deleted Successfully"));

	}

}
