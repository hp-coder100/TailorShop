package com.cts.hemant.tailorshop.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.hemant.tailorshop.entity.Appointment;
import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Measurement;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.MeasurementDto;
import com.cts.hemant.tailorshop.repository.AppointmentRepository;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.MeasurementRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.service.MeasurementService;
import com.cts.hemant.tailorshop.util.StaticStringValues;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MeasurementServiceImpl implements MeasurementService {

	@Autowired
	private MeasurementRepository measurementRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TailorRepository tailorRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	/**
	 * Creates a new measurement based on the provided MeasurementDto.
	 * 
	 * @param measurementDto Data transfer object containing measurement details.
	 * @return The created MeasurementDto with persisted data.
	 * @throws ResourceNotFoundException if the measurement already exists or
	 *                                   related entities are not found.
	 */
	@Override
	public MeasurementDto createMeasurement(MeasurementDto measurementDto) throws ResourceNotFoundException {
		// Log the creation request
		log.info(StaticStringValues.REQUESTING_MEASUREMENT_CREATE);

		// Check for existing measurement with the same ID
		if (measurementRepository.findById(measurementDto.getMeasurementId()).isPresent()) {
			throw new ResourceNotFoundException(
					StaticStringValues.MEASUREMENT_ALREADY_EXIST + measurementDto.getMeasurementId());
		}

		// Validate appointment existence
		Optional<Appointment> appointment = appointmentRepository.findById(measurementDto.getAppointmentId());
		if (appointment.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + measurementDto.getAppointmentId());
		}

		// Validate customer existence
		Optional<Customer> customer = customerRepository.findById(measurementDto.getCustomerId());
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + measurementDto.getCustomerId());
		}

		// Validate tailor existence
		Optional<Tailor> tailor = tailorRepository.findById(measurementDto.getShopId());
		if (tailor.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + measurementDto.getShopId());
		}

		// Map DTO to entity and set relationships
		Measurement measurement = measurementDto.mapToMeasurement();
		measurement.setAppointment(appointment.get());
		measurement.setCustomer(customer.get());
		measurement.setTailor(tailor.get());

		// Save the measurement and map back to DTO
		MeasurementDto savedMeasurementDto = measurementRepository.save(measurement).mapToMeasurementDto();

		// Log the successful creation
		log.debug(StaticStringValues.MEASUREMENT_CREATED_SUCCESSFULLY + " " + savedMeasurementDto);
		log.info(StaticStringValues.MEASUREMENT_CREATED_SUCCESSFULLY);
		return savedMeasurementDto;
	}

	/**
	 * Deletes a measurement by its ID.
	 * 
	 * @param measurementId The ID of the measurement to delete.
	 * @throws ResourceNotFoundException if the measurement is not found.
	 */
	@Override
	public void deleteMeasurement(long measurementId) throws ResourceNotFoundException {
		// Log the delete request
		log.info(StaticStringValues.REQUEST_MEASUREMENT_DELETEBY_ID + measurementId);

		// Check if the measurement exists
		Optional<Measurement> measurementOptional = measurementRepository.findById(measurementId);
		if (measurementOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.MEASUREMENT_DOES_NOT_EXIT_STRING + measurementId);
		}

		// Delete the measurement
		measurementRepository.delete(measurementOptional.get());

		// Log the successful deletion
		log.info(StaticStringValues.CATEGORY_DELETED_SUCCESSFULLY);
	}

	/**
	 * Updates an existing measurement based on the provided MeasurementDto.
	 * 
	 * @param measurementDto Data transfer object containing measurement details.
	 * @return The updated MeasurementDto with persisted data.
	 * @throws ResourceNotFoundException if the measurement does not exist.
	 */
	@Override
	public MeasurementDto updateMeasurement(MeasurementDto measurementDto) throws ResourceNotFoundException {
		// Log the update request
		log.info(StaticStringValues.REQUEST_MEASUREMENT_UPDATE);

		// Check for existing measurement with the same ID
		Optional<Measurement> measurementOptional = measurementRepository.findById(measurementDto.getMeasurementId());
		if (measurementOptional.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.MEASUREMENT_DOES_NOT_EXIT_STRING + measurementDto.getMeasurementId());
		}

		// Update details if provided
		Measurement measurement = measurementOptional.get();
		if (measurement.getDetails() != null)
			measurement.setDetails(measurementDto.getDetails());

		// Save the updated measurement and map back to DTO
		MeasurementDto updatedMeasurementDto = measurementRepository.save(measurement).mapToMeasurementDto();

		// Log the successful update
		log.debug(StaticStringValues.MEASUREMENT_UPDATED_SUCCESSFULLY + " " + updatedMeasurementDto);
		log.info(StaticStringValues.MEASUREMENT_UPDATED_SUCCESSFULLY);
		return updatedMeasurementDto;
	}

	/**
	 * Finds all measurements for a specific appointment ID.
	 * 
	 * @param appointmentId The ID of the appointment whose measurements are to be
	 *                      found.
	 * @return A list of MeasurementDto for the specified appointment.
	 * @throws ResourceNotFoundException if the appointment does not exist.
	 */
	@Override
	public List<MeasurementDto> findAllByCustomerIdAndAppointmentId(long customerId, long appointmentId)
			throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_FIND_ALL_MEASUREMENTS_APPOINTMENTID + appointmentId);

		// Validate customer existence
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Validate appointment existence
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
		if (appointment.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + appointmentId);
		}

		// Retrieve all measurements for the appointment, map to DTOs, and collect to a
		// list
		List<MeasurementDto> list = measurementRepository
				.findAllByCustomer_CustomerIdAndAppointment_AppointmentId(customerId, appointmentId).stream()
				.map(meas -> meas.mapToMeasurementDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	@Override
	public List<MeasurementDto> findAllByShopIdAndAppointmentId(long shopId, long appointmentId)
			throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_FIND_ALL_MEASUREMENTS_APPOINTMENTID + appointmentId);

		// Validate tailor existence
		Optional<Tailor> tailor = tailorRepository.findById(shopId);
		if (tailor.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}
		// Validate appointment existence
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
		if (appointment.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + appointmentId);
		}

		// Retrieve all measurements for the appointment, map to DTOs, and collect to a
		// list
		List<MeasurementDto> list = measurementRepository
				.findAllByTailor_ShopIdAndAppointment_AppointmentId(shopId, appointmentId).stream()
				.map(meas -> meas.mapToMeasurementDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	/**
	 * Finds a measurement by its ID.
	 * 
	 * @param measurementId The ID of the measurement to find.
	 * @return The found MeasurementDto.
	 * @throws ResourceNotFoundException if the measurement is not found.
	 */
	@Override
	public MeasurementDto findByMeasurementId(long measurementId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_MEASUREMENT_FINDBY_ID + measurementId);

		// Attempt to retrieve the measurement
		Optional<Measurement> measurementOptional = measurementRepository.findById(measurementId);
		if (measurementOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.MEASUREMENT_DOES_NOT_EXIT_STRING + measurementId);
		}

		// Map the entity to DTO
		MeasurementDto measurementDto = measurementOptional.get().mapToMeasurementDto();

		// Log the successful find
		log.debug(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY + " " + measurementDto);
		log.info(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY);
		return measurementDto;
	}

	/**
	 * Finds all measurements.
	 * 
	 * @return A list of all MeasurementDto.
	 */
	@Override
	public List<MeasurementDto> findAllMeasurement() {
		// Log the find all request
		log.info(StaticStringValues.REQUEST_FIND_ALL_MEASUREMENTS);

		// Retrieve all measurements, map to DTOs, and collect to a list
		List<MeasurementDto> list = measurementRepository.findAll().stream().map(meas -> meas.mapToMeasurementDto())
				.collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	/**
	 * Finds all measurements for a specific customer ID.
	 * 
	 * @param customerId The ID of the customer whose measurements are to be found.
	 * @return A list of MeasurementDto for the specified customer.
	 * @throws ResourceNotFoundException if the customer does not exist.
	 */
	@Override
	public List<MeasurementDto> findAllByCustomerId(long customerId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_FIND_ALL_MEASUREMENTS_CUSTOMERID + customerId);

		// Validate customer existence
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Retrieve all measurements for the customer, map to DTOs, and collect to a
		// list
		List<MeasurementDto> list = measurementRepository.findByCustomer_CustomerId(customerId).stream()
				.map(meas -> meas.mapToMeasurementDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	/**
	 * Finds all measurements for a specific shop ID.
	 * 
	 * @param shopId The ID of the shop whose measurements are to be found.
	 * @return A list of MeasurementDto for the specified shop.
	 * @throws ResourceNotFoundException if the shop does not exist.
	 */
	@Override
	public List<MeasurementDto> findAllByShopId(long shopId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_FIND_ALL_MEASUREMENTS_SHOP_ID + shopId);

		// Validate shop existence
		Optional<Customer> customer = customerRepository.findById(shopId);
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}

		// Retrieve all measurements for the shop, map to DTOs, and collect to a list
		List<MeasurementDto> list = measurementRepository.findByTailor_ShopId(shopId).stream()
				.map(meas -> meas.mapToMeasurementDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	@Override
	public List<MeasurementDto> findAllByCustomerIdAndShopId(long customerId, long shopId)
			throws ResourceNotFoundException {
		// Validate tailor existence
		Optional<Tailor> tailor = tailorRepository.findById(shopId);
		if (tailor.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}
		// Validate customer existence
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Retrieve all measurements for the appointment, map to DTOs, and collect to a
		// list
		List<MeasurementDto> list = measurementRepository
				.findAllByCustomer_CustomerIdAndTailor_ShopId(customerId, shopId).stream()
				.map(meas -> meas.mapToMeasurementDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.MEASUREMENT_FOUND_SUCCESSFULLY);
		return list;
	}

}
