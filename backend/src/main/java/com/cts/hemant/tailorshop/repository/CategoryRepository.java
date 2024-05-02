package com.cts.hemant.tailorshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.hemant.tailorshop.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	/**
	 * This method is used to find all Category entities associated with a specific
	 * Tailor based on the shopId.
	 * 
	 * @param shopId This is the ID of the shop whose categories are to be found.
	 * @return List<Category> This returns the list of found categories.
	 */
	List<Category> findAllByTailor_ShopId(long shopId);

}
