package com.cts.hemant.tailorshop.service;

import java.util.List;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.CategoryDto;

public interface CategoryService {
	
	public CategoryDto createCategory(CategoryDto categoryDto) throws ResourceNotFoundException;
	
	public CategoryDto findByCategoryId(long categoryId) throws ResourceNotFoundException;

	public void deleteCategory(long categoryId)  throws ResourceNotFoundException;

	public CategoryDto updateCategory(CategoryDto categoryDto)  throws ResourceNotFoundException;

	public List<CategoryDto> findAllCategories();

	public List<CategoryDto> findAllByShopId(long shopId) throws ResourceNotFoundException;
	
	
}
