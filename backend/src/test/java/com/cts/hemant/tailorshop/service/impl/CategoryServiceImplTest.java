package com.cts.hemant.tailorshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.hemant.tailorshop.entity.Category;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.CategoryDto;
import com.cts.hemant.tailorshop.repository.CategoryRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.util.StaticStringValues;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private TailorRepository tailorRepository;
	

	@InjectMocks
	private CategoryServiceImpl categoryService;

	private final Tailor TAILOR_1 = new Tailor(1L, "Kashif Umar", "Hello How are You doing", "Kashif@gmail.com",
			"1232323", null);

	private final Tailor TAILOR_2 = new Tailor(2L, "Kashif Umar", "Hello How are You doing", "Kashif@gmail.com",
			"1232323", null);

	private final Category CAT_1 = new Category(1L, "Kurta Pazam", "Slim Stylish Outfit", TAILOR_1, null);
	private final Category CAT_2 = new Category(2L, "Kurta Pazam", "Slim Stylish Outfit", TAILOR_2, null);
	private final Category CAT_3 = new Category(3L, "Kurta Pazam", "Slim Stylish Outfit", TAILOR_2, null);

	private final CategoryDto CATEGORY_DTO_1 = new CategoryDto(1L, "Kurta Pazam", "Slim Stylish Outfit", 1L, null);

	@Test
	void testCreateCategory() throws ResourceNotFoundException {
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));

		when(categoryRepository.save(CAT_1)).thenReturn(CAT_1);

		CategoryDto creatCategoryDto = categoryService.createCategory(CATEGORY_DTO_1);

		verify(categoryRepository, times(1)).save(CAT_1);

		assertThat(creatCategoryDto.getCategoryId()).isEqualTo(CATEGORY_DTO_1.getCategoryId());

	}

	@Test
	void testCreateCategory_whenCategoryIdIsInvalid() {

		when(categoryRepository.findById(1L)).thenReturn(Optional.of(CAT_1));
		
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			categoryService.createCategory(CATEGORY_DTO_1);
		});

		String actualMessage = exception.getMessage();
		verify(categoryRepository, never()).save(CAT_1);
		assertTrue(actualMessage.contains(StaticStringValues.CATEGORY_ALREADY_EXIST+1));
	}

	@Test
	void testCreateCategory_whenShopIdIsInvalid() {

		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			categoryService.createCategory(CATEGORY_DTO_1);
		});

		String actualMessage = exception.getMessage();

		verify(categoryRepository, never()).save(CAT_1);

		assertTrue(actualMessage.contains(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING+1));

	}

	@Test
	void testFindByCategoryId() throws ResourceNotFoundException {

		when(categoryRepository.findById(1L)).thenReturn(Optional.of(CAT_1));

		CategoryDto creatCategoryDto = categoryService.findByCategoryId(1L);

		verify(categoryRepository, times(1)).findById(1L);

		assertThat(creatCategoryDto.getCategoryId()).isEqualTo(1L);

	}

	@Test
	public void testFindByCategoryId_whenNoCategoryFound() {

		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			categoryService.findByCategoryId(1L);
		});

		String actualMessage = exception.getMessage();

		verify(categoryRepository, times(1)).findById(1L);

		assertTrue(actualMessage.contains(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING+1));
	}

	@Test
	public void testDeleteCategory() throws ResourceNotFoundException {

		when(categoryRepository.findById(1L)).thenReturn(Optional.of(CAT_1));

		doNothing().when(categoryRepository).delete(CAT_1);

		categoryService.deleteCategory(1L);

		verify(categoryRepository, times(1)).findById(1L);

		verify(categoryRepository, times(1)).delete(CAT_1);

	}

	@Test
	public void testDeleteCategory_whenCategoryIsInvalid() {

		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			categoryService.deleteCategory(1L);
		});

		String actualMessage = exception.getMessage();

		verify(categoryRepository, times(1)).findById(1L);

		verify(categoryRepository, never()).delete(CAT_1);

		assertTrue(actualMessage.contains(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING+1));
	}
	
	@Test
	
	public void testUpdateCategory() throws ResourceNotFoundException {
		
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(CAT_1));

		when(categoryRepository.save(CAT_1)).thenReturn(CAT_1);

		CategoryDto updateCategoryDto = categoryService.updateCategory(CATEGORY_DTO_1);

		verify(categoryRepository, times(1)).findById(1L);

		verify(categoryRepository, times(1)).save(CAT_1);
		
		assertThat(updateCategoryDto.getCategoryId()).isEqualTo(CATEGORY_DTO_1.getCategoryId());
	}

	@Test
	void testUpdateCategory_whenCategoryIdIsInvalid() {

		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			categoryService.updateCategory(CATEGORY_DTO_1);
		});

		String actualMessage = exception.getMessage();

		verify(categoryRepository, times(1)).findById(1L);

		verify(categoryRepository, never()).save(CAT_1);

		assertTrue(actualMessage.contains(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING+1));
	}

	@Test
	void testFindAllCategories() {
		when(categoryRepository.findAll()).thenReturn(Arrays.asList(CAT_1, CAT_2, CAT_3));
		
		List<CategoryDto> list = categoryService.findAllCategories();
		
		verify(categoryRepository, times(1)).findAll();
		
		assertThat(list.size()).isEqualTo(3);
	}
	
	@Test
	void testFindAllByShopId() throws ResourceNotFoundException {
		when(categoryRepository.findAllByTailor_ShopId(2L)).thenReturn(Arrays.asList(CAT_2, CAT_3));
		
		when(tailorRepository.findById(2L)).thenReturn(Optional.of(TAILOR_2));
		
		List<CategoryDto> list = categoryService.findAllByShopId(2L);
		
		verify(tailorRepository, times(1)).findById(2L);
		
		verify(categoryRepository, times(1)).findAllByTailor_ShopId(2L);
		
		assertThat(list.size()).isEqualTo(2);
		
		
	}

	@Test
	void testFindAllByShopId_whenShopIdIsInvalid() {

		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			categoryService.findAllByShopId(1L);
		});

		String actualMessage = exception.getMessage();
		
		verify(tailorRepository, times(1)).findById(1L);

		verify(categoryRepository, never()).findAllByTailor_ShopId(1L);

		assertTrue(actualMessage.contains(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING+1));
	}

}
