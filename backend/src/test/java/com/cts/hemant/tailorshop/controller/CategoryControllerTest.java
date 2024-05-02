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

import com.cts.hemant.tailorshop.payload.CategoryDto;
import com.cts.hemant.tailorshop.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters=false)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	private static ObjectMapper objectMapper;

	public String mapToJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	private final CategoryDto CAT_1 = new CategoryDto(1L, "Kurta Pazam", "Slim Stylish Outfit", 1L, null),
			CAT_2 = new CategoryDto(2L, "Kurta Pazam", "Slim Stylish Outfit", 2l, null),
			CAT_3 = new CategoryDto(3L, "Kurta Pazam", "Slim Stylish Outfit", 2l, null);

	@BeforeAll
	public static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Test
	void testCreateCategory() throws Exception {
		when(categoryService.createCategory(CAT_1)).thenReturn(CAT_1);
		mockMvc.perform(post("/category/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(CAT_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.categoryId").value(1L));
	}
	
	@Test
	void testFindCategories_withShopId() throws Exception {
		when(categoryService.findAllByShopId(1L)).thenReturn(Arrays.asList(CAT_1));	
		mockMvc.perform(get("/category/findAll?shopId=1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));

	}

	@Test
	void testFindCateogries() throws Exception {
		when(categoryService.findAllCategories()).thenReturn(Arrays.asList(CAT_1, CAT_2, CAT_3));
		
		mockMvc.perform(get("/category/findAll").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(3));
	}

	@Test
	void testFindByCategoryId() throws Exception {
		when(categoryService.findByCategoryId(1L)).thenReturn(CAT_1);
		mockMvc.perform(get("/category/find/{categoryId}", 1).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.categoryId").value(1L));
	}

	@Test
	void testUpdateCategory() throws JsonProcessingException, Exception {
		when(categoryService.updateCategory(CAT_1)).thenReturn(CAT_1);
		
		mockMvc.perform(put("/category/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(CAT_1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.categoryId").value(1L));
		
	}

	@Test
	void testDeleteCategory() throws Exception {
		doNothing().when(categoryService).deleteCategory(1L);
		mockMvc.perform(delete("/category/delete/{categoryId}", 1L).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$").value("Category Successfully Deleted"));

	}

}
