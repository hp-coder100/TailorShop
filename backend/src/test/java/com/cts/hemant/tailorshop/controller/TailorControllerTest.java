package com.cts.hemant.tailorshop.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.cts.hemant.tailorshop.payload.TailorDto;
import com.cts.hemant.tailorshop.service.TailorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters=false)
class TailorControllerTest {

	private final TailorDto TAILOR_DTO_1 = new TailorDto(1L, "Kashif Umar", "Hello How are You doing",
			"Kashif@gmail.com", "1232323", null);

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TailorService tailorService;

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
	void testCreateTailor() throws Exception {
		when(tailorService.createTailor(TAILOR_DTO_1)).thenReturn(TAILOR_DTO_1);
		mockMvc.perform(post("/tailor/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(TAILOR_DTO_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.shopId").value(1L));
	}

	@Test
	void testFindAllTailors() throws Exception {
		when(tailorService.findAllTailors()).thenReturn(Arrays.asList(TAILOR_DTO_1));
		mockMvc.perform(get("/tailor/findAll").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));
	}

	@Test
	void testFindAllTailors_withTailorName() throws Exception {
		when(tailorService.findByTailorName("Kashif Umar")).thenReturn(TAILOR_DTO_1);
		mockMvc.perform(get("/tailor/findAll?tailorName=Kashif Umar").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.shopId").value(1));
	}

	
	@Test
	void testUpdateTailor() throws Exception {
		when(tailorService.updateTailor(TAILOR_DTO_1)).thenReturn(TAILOR_DTO_1);
		mockMvc.perform(put("/tailor/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(TAILOR_DTO_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.shopId").value(1L));
	}

	@Test
	void testFindByShopId() throws Exception {
		when(tailorService.findByShopId(1L)).thenReturn(TAILOR_DTO_1);
		mockMvc.perform(get("/tailor/find/{shopId}", 1L).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andExpect(jsonPath("$.shopId").value(1));

	}

	@Test
	void testDeleteTailor() throws Exception {
		doNothing().when(tailorService).deleteTailor(1L);

		mockMvc.perform(delete("/tailor/delete/{shopId}", 1L).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$").value("Tailor deleted Successfully"));

	}

}
