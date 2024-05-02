package com.cts.hemant.tailorshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.CategoryDto;
import com.cts.hemant.tailorshop.service.CategoryService;
/**
 * Controller class for handling category-related operations.
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	 private CategoryService categoryService;
	/**
     * This method is used to create a new category.
     * @param categoryDto This is the category data to be created.
     * @return ResponseEntity This returns the created category.
     * @throws ResourceNotFoundException This exception is thrown when the category to be created is not found.
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createCategory(@RequestBody CategoryDto categoryDto) throws ResourceNotFoundException {
        CategoryDto categoryDto2 = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(categoryDto2, HttpStatus.CREATED);
    }

    /**
     * This method is used to find categories by shop ID or find all categories if no shop ID is provided.
     * @param shopId This is the ID of the shop whose categories are to be found. If not provided, all categories are found.
     * @return ResponseEntity This returns the list of found categories.
     * @throws ResourceNotFoundException This exception is thrown when the categories are not found.
     */
    @GetMapping("/findAll")
    public ResponseEntity<Object> findCateogries(
            @RequestParam(value = "shopId", required = false, defaultValue = "0") long shopId) throws ResourceNotFoundException {
        List<CategoryDto> categoryDtoList = null;

        if (shopId != 0) {
            categoryDtoList = categoryService.findAllByShopId(shopId);
        } else {
            categoryDtoList = categoryService.findAllCategories();
        }
        return new ResponseEntity<Object>(categoryDtoList, HttpStatus.OK);
    }

    /**
     * This method is used to find a category by its ID.
     * @param categoryId This is the ID of the category to be found.
     * @return ResponseEntity This returns the found category.
     * @throws ResourceNotFoundException This exception is thrown when the category is not found.
     */
    @GetMapping("/find/{categoryId}")
    public ResponseEntity<Object> findByCategoryId(@PathVariable long categoryId) throws ResourceNotFoundException {
        CategoryDto categoryDto = categoryService.findByCategoryId(categoryId);
        return new ResponseEntity<Object>(categoryDto, HttpStatus.OK);
    }

    /**
     * This method is used to update a category.
     * @param categoryDto This is the category data to be updated.
     * @return ResponseEntity This returns the updated category.
     * @throws ResourceNotFoundException This exception is thrown when the category to be updated is not found.
     */
    @PutMapping("/update")
    public ResponseEntity<Object> updateCategory(@RequestBody CategoryDto categoryDto) throws ResourceNotFoundException {
        CategoryDto categoryDto2 = categoryService.updateCategory(categoryDto);
        return new ResponseEntity<Object>(categoryDto2, HttpStatus.OK);
    }

    /**
     * This method is used to delete a category by its ID.
     * @param categoryId This is the ID of the category to be deleted.
     * @return ResponseEntity This returns a success message after deletion.
     * @throws ResourceNotFoundException This exception is thrown when the category to be deleted is not found.
     */
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long categoryId) throws ResourceNotFoundException {
    	System.out.println(categoryId);
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<Object>("{\"message\": \"Category Successfully Deleted\"}", HttpStatus.OK);
    }


}
