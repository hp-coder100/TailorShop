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

import com.cts.hemant.tailorshop.payload.AppointmentDto;
import com.cts.hemant.tailorshop.service.AppointmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters=false)
class AppointmentControllerTest {
	@Autowired
	private MockMvc mockMvc;
	

	@MockBean
	private AppointmentService appointmentService;

	private static ObjectMapper objectMapper;

	public String mapToJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	private final AppointmentDto APPOINTMENT_DTO_1 = new AppointmentDto(1L, LocalDate.parse("2023-05-11"), "Pending", 1,
			1, 1), APPOINTMENT_DTO_2 = new AppointmentDto(2L, LocalDate.parse("2023-05-20"), "Pending", 1, 2, 1),
			APPOINTMENT_DTO_3 = new AppointmentDto(3L, LocalDate.parse("2023-06-24"), "Success", 2, 1, 2);

	@BeforeAll
	public static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Test
	public void testRequestAppointment_Success() throws Exception {

		when(appointmentService.createAppointment(APPOINTMENT_DTO_1)).thenReturn(APPOINTMENT_DTO_1);

		mockMvc.perform(post("/appointment/create").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(APPOINTMENT_DTO_1)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.appointmentId").value(1L))
				.andExpect(jsonPath("$.status").value("Pending"));
	}
	
	@Test
	void test_getAppointment() throws Exception{
		when(appointmentService.findAppointmentByAppointmentId(1L)).thenReturn(APPOINTMENT_DTO_1);
		mockMvc.perform(get("/appointment/find/{appointmentId}", 1).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.categoryId").value(1L));
	}

	@Test
	public void test_getAllAppointments_withoutAnyRequestParam() throws Exception {
		when(appointmentService.findAllAppointments())
				.thenReturn(Arrays.asList(APPOINTMENT_DTO_1, APPOINTMENT_DTO_2, APPOINTMENT_DTO_3));

		mockMvc.perform(get("/appointment/findAll").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(3));

	}

	@Test
	public void test_getAllAppointments_withAnyRequestParamCustomerId() throws Exception {
		when(appointmentService.findAllByCustomerId(1L)).thenReturn(Arrays.asList(APPOINTMENT_DTO_1));

		mockMvc.perform(get("/appointment/findAll?customerId=1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$.[0].customerId").value(1L));

	}

	@Test
	public void test_getAllAppointments_withAnyRequestParamShopId() throws Exception {
		when(appointmentService.findAllByShopId(2L)).thenReturn(Arrays.asList(APPOINTMENT_DTO_2));

		mockMvc.perform(get("/appointment/findAll?shopId=2").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$.[0].appointmentId").value(2L))
				.andExpect(jsonPath("$.[0].shopId").value(2L));

	}

	@Test
	public void test_getAllAppointments_withAnyRequestParamCategoryId() throws Exception {
		when(appointmentService.findAllByCategoryId(2L)).thenReturn(Arrays.asList(APPOINTMENT_DTO_3));

		mockMvc.perform(get("/appointment/findAll?categoryId=2").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$.[0].appointmentId").value(3L))
				.andExpect(jsonPath("$.[0].shopId").value(1L));

	}

	
	@Test
	public void test_getAllAppointments_withAnyRequestParamDate() throws Exception {
		when(appointmentService.findAllByDate(LocalDate.parse("2023-06-24")))
				.thenReturn(Arrays.asList(APPOINTMENT_DTO_3));

		mockMvc.perform(get("/appointment/findAll?date=2023-06-24").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$.[0].appointmentId").value(3L)).andExpect(jsonPath("$.[0].shopId").value(1L));

	}

	@Test
	public void deleteAppintment_withAppointmentId() throws  Exception {

		doNothing().when(appointmentService).deleteAppointment(1L);

		mockMvc.perform(delete("/appointment/delete/{appointmentId}", 1L).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").value("Appointment Successfully Deleted"));

	}

	@Test
	public void updateAppointment_withValidAppointment() throws Exception {

		when(appointmentService.updateAppointment(APPOINTMENT_DTO_1)).thenReturn(APPOINTMENT_DTO_1);

		mockMvc.perform(put("/appointment/update").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(APPOINTMENT_DTO_1)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.appointmentId").value(1L)).andExpect(jsonPath("$.status").value("Pending"));
	}

//	@Test
//	public void updateAppointment_withInvalidAppointment() throws Exception {
//
//		when(appointmentService.updateAppointment(APPOINTMENT_DTO_1))
//				.thenThrow(new ResourceNotFoundException("Appointment doesn't exist with appointment Id:1"));
//
//		mockMvc.perform(put("/appointment").contentType(MediaType.APPLICATION_JSON)
//				.content(mapToJson(APPOINTMENT_DTO_1)).accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isNotFound())
//				.andExpect(jsonPath("$.message").value("Appointment doesn't exist with appointment Id:1"));
//	}

}
