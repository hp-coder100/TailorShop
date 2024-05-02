package com.cts.hemant.tailorshop.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.hemant.tailorshop.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	/**
	 * This method is used to find all Payment entities associated with a specific
	 * Appointment based on the appointmentId.
	 * 
	 * @param appointmentId This is the ID of the appointment whose payments are to
	 *                      be found.
	 * @return List<Payment> This returns the list of found payments.
	 */
	public List<Payment> findAllByAppointment_AppointmentId(long appointmentId);

	/**
	 * This method is used to find all Payment entities by their status.
	 * 
	 * @param status This is the status of the payments to be found.
	 * @return List<Payment> This returns the list of found payments.
	 */
	public List<Payment> findAllByStatus(String status);

	/**
	 * This method is used to find all Payment entities by their payment date.
	 * 
	 * @param date This is the date of the payments to be found.
	 * @return List<Payment> This returns the list of found payments.
	 */
	public List<Payment> findAllByPaymentDate(LocalDate date);
	
	
	public List<Payment> findAllByAppointment_Customer_CustomerId(long customerId);
	
	public List<Payment> findAllByAppointment_Customer_CustomerIdAndStatus(long customerId, String status);

	public List<Payment> findAllByAppointment_Customer_CustomerIdAndPaymentDate(long customerId, LocalDate date);
	
	
	public List<Payment> findAllByAppointment_Tailor_ShopId(long shopId);
	
	public List<Payment> findAllByAppointment_Tailor_ShopIdAndStatus(long shopId, String status);
	public List<Payment> findAllByAppointment_Tailor_ShopIdAndPaymentDate(long shopId, LocalDate date);
	
}
