package com.cts.hemant.tailorshop.payload;

import com.cts.hemant.tailorshop.entity.Category;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

	private long categoryId;
	private String categoryName;
	private String details;

	private long shopId;
	

	private String imageUrl;
	
	public Category mapToCategory() {
		Category category = new Category(categoryId, categoryName, details, null, imageUrl);
		return category;
	}
}
