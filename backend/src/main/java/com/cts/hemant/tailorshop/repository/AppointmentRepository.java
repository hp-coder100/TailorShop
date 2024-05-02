package com.cts.hemant.tailorshop.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.hemant.tailorshop.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	/**
	 * This method is used to find all appointments by customer ID.
	 * 
	 * @param customerId This is the ID of the customer whose appointments are to be
	 *                   found.
	 * @return List<Appointment> This returns the list of found appointments.
	 */
	public List<Appointment> findAllByCustomer_CustomerId(long customerId);

	public List<Appointment> findAllByCustomer_CustomerIdAndTailor_ShopId(long customerId, long shopId);

	public List<Appointment> findAllByCustomer_CustomerIdAndAppointmentDate(long customerId, LocalDate date);

	public List<Appointment> findAllByCustomer_CustomerIdAndStatus(long customerId, String status);

	public List<Appointment> findAllByCustomer_CustomerIdAndCategory_CategoryId(long customerId, long categoryId);

	/**
	 * This method is used to find all appointments by appointment date.
	 * 
	 * @param date This is the date of the appointments to be found.
	 * @return List<Appointment> This returns the list of found appointments.
	 */
	public List<Appointment> findAllByAppointmentDate(LocalDate date);

	/**
	 * This method is used to find all appointments by category ID.
	 * 
	 * @param categoryId This is the ID of the category whose appointments are to be
	 *                   found.
	 * @return List<Appointment> This returns the list of found appointments.
	 */
	public List<Appointment> findAllByCategory_CategoryId(long categoryId);

	/**
	 * This method is used to find all appointments by shop ID.
	 * 
	 * @param shopId This is the ID of the shop whose appointments are to be found.
	 * @return List<Appointment> This returns the list of found appointments.
	 */
	public List<Appointment> findAllByTailor_ShopId(long shopId);

	public List<Appointment> findAllByTailor_ShopIdAndAppointmentDate(long shopId, LocalDate date);

	public List<Appointment> findAllByTailor_ShopIdAndStatus(long shopId, String status);

	public List<Appointment> findAllByTailor_ShopIdAndCategory_CategoryId(long shopId, long categoryId);

}