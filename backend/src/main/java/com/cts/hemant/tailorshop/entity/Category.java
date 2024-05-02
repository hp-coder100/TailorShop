package com.cts.hemant.tailorshop.entity;

import com.cts.hemant.tailorshop.payload.CategoryDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private long categoryId;
	@Column(name = "category_name", nullable = false)
	private String categoryName;
	@Column(name = "details", nullable = false)
	private String details;

	@ManyToOne
	@JoinColumn(name = "shop_id")
	private Tailor tailor;
	
	private String imageUrl;

	public CategoryDto mapToCategoryDto() {
		CategoryDto categoryDto = new CategoryDto(categoryId, categoryName, details, tailor.getShopId(), imageUrl);
		return categoryDto;
	}
}
