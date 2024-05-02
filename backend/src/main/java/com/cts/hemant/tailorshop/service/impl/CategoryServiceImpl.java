package com.cts.hemant.tailorshop.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.hemant.tailorshop.entity.Category;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.CategoryDto;
import com.cts.hemant.tailorshop.repository.CategoryRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.service.CategoryService;
import com.cts.hemant.tailorshop.util.StaticStringValues;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TailorRepository tailorRepository;

	/**
	 * Creates a new category based on the provided CategoryDto.
	 * 
	 * @param categoryDto Data transfer object containing category details.
	 * @return The created CategoryDto with persisted data.
	 * @throws ResourceNotFoundException if the category already exists or related
	 *                                   entities are not found.
	 */
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) throws ResourceNotFoundException {
		// Log the creation request
		log.info(StaticStringValues.REQUESTING_CATEGORY_CREATE);

		// Check for existing category with the same ID
		if (categoryRepository.findById(categoryDto.getCategoryId()).isPresent()) {
			throw new ResourceNotFoundException(
					StaticStringValues.CATEGORY_ALREADY_EXIST + categoryDto.getCategoryId());
		}

		// Validate tailor existence
		Optional<Tailor> tailorOptional = tailorRepository.findById(categoryDto.getShopId());
		if (tailorOptional.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + categoryDto.getShopId());
		}

		// Map DTO to entity and set relationships
		Category category = categoryDto.mapToCategory();
		category.setTailor(tailorOptional.get());

		// Save the category and map back to DTO
		CategoryDto savedCategoryDto = categoryRepository.save(category).mapToCategoryDto();

		// Log the successful creation
		log.debug(StaticStringValues.CATEGORY_CREATED_SUCCESSFULLY + " " + savedCategoryDto);
		log.info(StaticStringValues.CATEGORY_CREATED_SUCCESSFULLY);
		return savedCategoryDto;
	}

	/**
	 * Finds a category by its ID.
	 * 
	 * @param categoryId The ID of the category to find.
	 * @return The found CategoryDto.
	 * @throws ResourceNotFoundException if the category is not found.
	 */
	@Override
	public CategoryDto findByCategoryId(long categoryId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_CATEGORY_FINDBY_ID + categoryId);

		// Attempt to retrieve the category
		Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
		if (categoryOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING + categoryId);
		}

		// Map the entity to DTO
		CategoryDto categoryDto = categoryOptional.get().mapToCategoryDto();

		// Log the successful find
		log.debug(StaticStringValues.CATEGORY_FOUND_SUCCESSFULLY + " " + categoryDto);
		log.info(StaticStringValues.CATEGORY_FOUND_SUCCESSFULLY);
		return categoryDto;
	}

	/**
	 * Deletes a category by its ID.
	 * 
	 * @param categoryId The ID of the category to delete.
	 * @throws ResourceNotFoundException if the category is not found.
	 */
	@Override
	public void deleteCategory(long categoryId) throws ResourceNotFoundException {
		// Log the delete request
		log.info(StaticStringValues.REQUEST_CATEGORY_DELETEBY_ID + categoryId);

		// Check if the category exists
		Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
		if (categoryOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING + categoryId);
		}

		// Delete the category
		categoryRepository.delete(categoryOptional.get());

		// Log the successful deletion
		log.info(StaticStringValues.CATEGORY_DELETED_SUCCESSFULLY);
	}

	/**
	 * Updates an existing category based on the provided CategoryDto.
	 * 
	 * @param categoryDto Data transfer object containing category details.
	 * @return The updated CategoryDto with persisted data.
	 * @throws ResourceNotFoundException if the category does not exist.
	 */
	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto) throws ResourceNotFoundException {
		// Log the update request
		log.info(StaticStringValues.REQUEST_CATEGORY_UPDATE);

		// Check for existing category with the same ID
		Optional<Category> categoryOptional = categoryRepository.findById(categoryDto.getCategoryId());
		if (categoryOptional.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING + categoryDto.getCategoryId());
		}

		// Update details if provided
		Category category = categoryOptional.get();
		if (categoryDto.getCategoryName() != null)
			category.setCategoryName(categoryDto.getCategoryName());
		if (categoryDto.getDetails() != null)
			category.setDetails(categoryDto.getDetails());

		// Save the updated category and map back to DTO
		CategoryDto updateCategoryDto = categoryRepository.save(category).mapToCategoryDto();

		// Log the successful update
		log.debug(StaticStringValues.CATEGORY_UPDATED_SUCCESSFULLY + updateCategoryDto);
		log.info(StaticStringValues.CATEGORY_UPDATED_SUCCESSFULLY);
		return updateCategoryDto;
	}

	/**
	 * Finds all categories.
	 * 
	 * @return A list of all CategoryDto.
	 */
	@Override
	public List<CategoryDto> findAllCategories() {
		// Log the find all request
		log.info(StaticStringValues.REQUEST_FIND_ALL_CATEGORY);

		// Retrieve all categories, map to DTOs, and collect to a list
		List<CategoryDto> list = categoryRepository.findAll().stream().map(app -> app.mapToCategoryDto())
				.collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.CATEGORY_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.CATEGORY_FOUND_SUCCESSFULLY);
		return list;
	}

	/**
	 * Finds all categories for a specific shop ID.
	 * 
	 * @param shopId The ID of the shop whose categories are to be found.
	 * @return A list of CategoryDto for the specified shop.
	 * @throws ResourceNotFoundException if the shop does not exist.
	 */
	@Override
	public List<CategoryDto> findAllByShopId(long shopId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_FIND_ALL_CATEGORY_BY_SHOP_ID + shopId);

		// Validate shop existence
		Optional<Tailor> tailorOptional = tailorRepository.findById(shopId);
		if (tailorOptional.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}

		// Retrieve all categories for the shop, map to DTOs, and collect to a list
		List<CategoryDto> list = categoryRepository.findAllByTailor_ShopId(shopId).stream()
				.map(app -> app.mapToCategoryDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.CATEGORY_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.CATEGORY_FOUND_SUCCESSFULLY);
		return list;
	}

}
