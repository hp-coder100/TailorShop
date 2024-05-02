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

import com.cts.hemant.tailorshop.payload.MeasurementDto;
import com.cts.hemant.tailorshop.service.MeasurementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters=false)
class MeasurementControllerTest {
	private final MeasurementDto MEAS_DTO_1 = new MeasurementDto(1L, "sljflasljdfl", 1L, 1L, 1L);

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MeasurementService measurementService;

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
	void testCreateMeasurement() throws Exception {
		when(measurementService.createMeasurement(MEAS_DTO_1)).thenReturn(MEAS_DTO_1);
		mockMvc.perform(post("/measurement/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(MEAS_DTO_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.measurementId").value(1L));
	}

	@Test
	void testFindMeasurements() throws Exception {
		when(measurementService.findAllMeasurement()).thenReturn(Arrays.asList(MEAS_DTO_1));
		mockMvc.perform(get("/measurement/findAll").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));
	}

//	@Test
//	void testFindMeasurements_withAppointmentId() throws Exception {
//		when(measurementService.findByAppointmentId(1L)).thenReturn(Arrays.asList(MEAS_DTO_1));
//		mockMvc.perform(get("/measurement?appointmentId=1").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
//	}

	@Test
	void testFindMeasurements_withCustomerId() throws Exception {
		when(measurementService.findAllByCustomerId(1L)).thenReturn(Arrays.asList(MEAS_DTO_1));
		mockMvc.perform(get("/measurement/findAll?customerId=1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}
	
	@Test
	void testFindMeasurements_withShopId() throws Exception {
		when(measurementService.findAllByShopId(1L)).thenReturn(Arrays.asList(MEAS_DTO_1));
		mockMvc.perform(get("/measurement/findAll?shopId=1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}

	@Test
	void testFindMeasurements_withMeasurementId() throws Exception {
		when(measurementService.findByMeasurementId(1L)).thenReturn(MEAS_DTO_1);
		mockMvc.perform(get("/measurement/find/{measurementId}",1).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.measurementId").value(1L));

	}

	
	@Test
	void testUpdateMeasurement() throws Exception {
		when(measurementService.updateMeasurement(MEAS_DTO_1)).thenReturn(MEAS_DTO_1);
		mockMvc.perform(put("/measurement/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(MEAS_DTO_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.measurementId").value(1L));
	}

	@Test
	void testDeleteMeasurement() throws Exception {
		doNothing().when(measurementService).deleteMeasurement(1L);

		mockMvc.perform(delete("/measurement/delete/{measurementId}", 1L).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").value("Measurement deleted Successfully"));

	}

}
