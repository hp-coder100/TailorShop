package com.cts.hemant.tailorshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.hemant.tailorshop.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	/**
	 * This method is used to find a Customer entity by its email.
	 * 
	 * @param email This is the email of the customer to be found.
	 * @return Optional<Customer> This returns an Optional that contains the found
	 *         customer if it exists, or is empty if the customer is not found.
	 */
	public Optional<Customer> findByEmail(String email);
	
	

}
