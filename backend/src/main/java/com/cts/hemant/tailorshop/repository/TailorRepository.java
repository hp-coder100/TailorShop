package com.cts.hemant.tailorshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.hemant.tailorshop.entity.Tailor;

@Repository
public interface TailorRepository extends JpaRepository<Tailor, Long> {

	/**
	 * This method is used to find a Tailor entity by its email.
	 * 
	 * @param email This is the email of the tailor to be found.
	 * @return Optional<Tailor> This returns an Optional that contains the found
	 *         tailor if it exists, or is empty if the tailor is not found.
	 */
	public Optional<Tailor> findByEmail(String email);

	/**
	 * This method is used to find a Tailor entity by its name.
	 * 
	 * @param tailorName This is the name of the tailor to be found.
	 * @return Optional<Tailor> This returns an Optional that contains the found
	 *         tailor if it exists, or is empty if the tailor is not found.
	 */
	public Optional<Tailor> findByTailorName(String tailorName);
}