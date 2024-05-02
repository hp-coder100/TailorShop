package com.cts.hemant.tailorshop.controller;

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
import com.cts.hemant.tailorshop.payload.MeasurementDto;
import com.cts.hemant.tailorshop.service.MeasurementService;

/**
 * Controller class for handling measurement-related operations.
 */
@RestController
@RequestMapping("/measurement")
public class MeasurementController {
	// Inject the MeasurementService into this class using Spring's Autowired
	// annotation
	@Autowired
	private MeasurementService measurementService;

	/**
	 * Handles the creation of a new measurement.
	 *
	 * @param measurementDto The measurement data transfer object containing the
	 *                       details of the new measurement.
	 * @return A ResponseEntity with the created measurement DTO and the HTTP status
	 *         CREATED.
	 * @throws ResourceNotFoundException If a required resource is not found during
	 *                                   the measurement creation process.
	 */
	@PostMapping("/create")
	public ResponseEntity<Object> createMeasurement(@RequestBody MeasurementDto measurementDto)
			throws ResourceNotFoundException {

		MeasurementDto measurementDto2 = measurementService.createMeasurement(measurementDto);
		return new ResponseEntity<>(measurementDto2, HttpStatus.CREATED);

	}

	/**
	 * Retrieve measurements based on shopId, appointmentId, or customerId.
	 * 
	 * @param shopId        The ID of the shop. Default is 0.
	 * @param appointmentId The ID of the appointment. Default is 0.
	 * @param customerId    The ID of the customer. Default is 0.
	 * @return ResponseEntity containing the measurement data.
	 * @throws ResourceNotFoundException if the requested resource is not found.
	 */
	@GetMapping("/findAll")
	public ResponseEntity<Object> findMeasurements()
			throws ResourceNotFoundException {
		Object measurementDtos = null;

			measurementDtos = measurementService.findAllMeasurement();
		
		return new ResponseEntity<>(measurementDtos, HttpStatus.OK);

	}
	
	
	@GetMapping("/findAll/customer/{customerId}")
	public ResponseEntity<Object> findMeasurementsForCustomer(@PathVariable("customerId") long customerId,
			@RequestParam(value = "shopId", required = false, defaultValue = "0") long shopId,
			@RequestParam(value = "appointmentId", required = false, defaultValue = "0") long appointmentId)
			throws ResourceNotFoundException {
		Object measurementDtos = null;

		if (shopId != 0) {
			measurementDtos = measurementService.findAllByCustomerIdAndShopId(customerId, shopId);
		} else if (appointmentId != 0) {
			measurementDtos = measurementService.findAllByCustomerIdAndAppointmentId(customerId,appointmentId);
		} 
		else {
			measurementDtos = measurementService.findAllByCustomerId(customerId);
		}
		return new ResponseEntity<>(measurementDtos, HttpStatus.OK);

	}
	
	@GetMapping("/findAll/tailor/{shopId}")
	public ResponseEntity<Object> findMeasurementsForTailor(@PathVariable("shopId") long shopId,
			@RequestParam(value = "customerId", required = false, defaultValue = "0") long customerId,
			@RequestParam(value = "appointmentId", required = false, defaultValue = "0") long appointmentId)
			throws ResourceNotFoundException {
		Object measurementDtos = null;

		if (customerId != 0) {
			measurementDtos = measurementService.findAllByCustomerIdAndShopId(customerId, shopId);
		} else if (appointmentId != 0) {
			measurementDtos = measurementService.findAllByShopIdAndAppointmentId(shopId,appointmentId);
		} 
		else {
			measurementDtos = measurementService.findAllByShopId(shopId);
		}
		return new ResponseEntity<>(measurementDtos, HttpStatus.OK);

	}
	
	/**
	 * This method is used to get a measurement by its ID.
	 * 
	 * @param measurementId This is the ID of the measurement to be fetched.
	 * @return ResponseEntity This returns the fetched measurement.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   measurement is not found.
	 */
	@GetMapping("/find/{measurementId}")
	public ResponseEntity<Object> updateMeasurement(@PathVariable("measurementId") long measurementId)
			throws ResourceNotFoundException {
		MeasurementDto measurementDto2 = measurementService.findByMeasurementId(measurementId);
		return new ResponseEntity<>(measurementDto2, HttpStatus.OK);
	}

	/**
	 * This method is used to update a measurement.
	 * 
	 * @param measurementDto This is the measurement data to be updated.
	 * @return ResponseEntity This returns the updated measurement.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   measurement to be updated is not found.
	 */
	@PutMapping("/update")
	public ResponseEntity<Object> updateMeasurement(@RequestBody MeasurementDto measurementDto)
			throws ResourceNotFoundException {
		MeasurementDto measurementDto2 = measurementService.updateMeasurement(measurementDto);
		return new ResponseEntity<>(measurementDto2, HttpStatus.OK);
	}

	/**
	 * This method is used to delete a measurement by its ID.
	 * 
	 * @param measurementId This is the ID of the measurement to be deleted.
	 * @return ResponseEntity This returns a success message after deletion.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   measurement to be deleted is not found.
	 */
	@DeleteMapping("/delete/{measurementId}")
	public ResponseEntity<Object> deleteMeasurement(@PathVariable("measurementId") long measurementId)
			throws ResourceNotFoundException {
		measurementService.deleteMeasurement(measurementId);
		return new ResponseEntity<>("Measurement deleted Successfully", HttpStatus.OK);
	}

}
