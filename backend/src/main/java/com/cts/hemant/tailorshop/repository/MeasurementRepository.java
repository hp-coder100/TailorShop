package com.cts.hemant.tailorshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.hemant.tailorshop.entity.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

	/**
	 * This method is used to find all Measurement entities associated with a
	 * specific Customer based on the customerId.
	 * 
	 * @param customerId This is the ID of the customer whose measurements are to be
	 *                   found.
	 * @return List<Measurement> This returns the list of found measurements.
	 */
	public List<Measurement> findByCustomer_CustomerId(long customerId);

	/**
	 * This method is used to find all Measurement entities associated with a
	 * specific Appointment based on the appointmentId.
	 * 
	 * @param appointmentId This is the ID of the appointment whose measurements are
	 *                      to be found.
	 * @return List<Measurement> This returns the list of found measurements.
	 */
	public List<Measurement> findByAppointment_AppointmentId(long appointmentId);

	/**
	 * This method is used to find all Measurement entities associated with a
	 * specific Tailor based on the shopId.
	 * 
	 * @param shopId This is the ID of the shop whose measurements are to be found.
	 * @return List<Measurement> This returns the list of found measurements.
	 */
	public List<Measurement> findByTailor_ShopId(long shopId);
	
	List<Measurement>  findAllByCustomer_CustomerIdAndTailor_ShopId(long customerId, long shopId);
	List<Measurement>  findAllByTailor_ShopIdAndAppointment_AppointmentId(long shopId, long appointmentId);
	List<Measurement>  findAllByCustomer_CustomerIdAndAppointment_AppointmentId(long customerId, long appointmentId);
}

