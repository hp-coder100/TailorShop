package com.cts.hemant.tailorshop.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.cts.hemant.tailorshop.payload.AppointmentDto;
import com.cts.hemant.tailorshop.service.AppointmentService;

/**
 * Controller class for handling appointment-related operations.
 */
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	/**
	 * This method is used to request a new appointment.
	 * 
	 * @param appointmentDto This is the appointment data to be created.
	 * @return ResponseEntity This returns the created appointment.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   appointment to be created is not found.
	 */
	@PostMapping("/create")
	public ResponseEntity<Object> requestAppointment(@RequestBody AppointmentDto appointmentDto)
			throws ResourceNotFoundException {

		AppointmentDto appointment2 = appointmentService.createAppointment(appointmentDto);
		return new ResponseEntity<>(appointment2, HttpStatus.CREATED);

	}

	/**
	 * This method is used to get all appointments or filter appointments by
	 * customer ID, shop ID, category ID, or date.
	 * 
	 * @param customerId This is the ID of the customer whose appointments are to be
	 *                   found. If not provided, all appointments are found.
	 * @param shopId     This is the ID of the shop whose appointments are to be
	 *                   found. If not provided, all appointments are found.
	 * @param categoryId This is the ID of the category whose appointments are to be
	 *                   found. If not provided, all appointments are found.
	 * @param date       This is the date of the appointments to be found. If not
	 *                   provided, all appointments are found.
	 * @return ResponseEntity This returns the list of found appointments.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   appointments are not found.
	 */
	@GetMapping("/findAll")
	public ResponseEntity<Object> getAllAppointments(
			@RequestParam(value = "customerId", required = false, defaultValue = "0") long customerId,
			@RequestParam(value = "shopId", required = false, defaultValue = "0") long shopId,
			@RequestParam(value = "categoryId", required = false, defaultValue = "0") long categoryId,
			@RequestParam(value = "date", required = false) LocalDate date) throws ResourceNotFoundException {

		List<AppointmentDto> appointmentDtos = null;
		if (customerId != 0) {
			appointmentDtos = appointmentService.findAllByCustomerId(customerId);
		} else if (shopId != 0) {
			appointmentDtos = appointmentService.findAllByShopId(shopId);
		} else if (categoryId != 0) {
			appointmentDtos = appointmentService.findAllByCategoryId(categoryId);
		} else if (date != null) {
			appointmentDtos = appointmentService.findAllByDate(date);
		} else {
			appointmentDtos = appointmentService.findAllAppointments();
		}
		return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);

	}

	/**
	 * This method is used to get an appointment by its ID.
	 * 
	 * @param appointmentId This is the ID of the appointment to be found.
	 * @return ResponseEntity This returns the found appointment.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   appointment is not found.
	 */
	@GetMapping("/find/{appointmentId}")
	public ResponseEntity<Object> getAppintment(@PathVariable("appointmentId") long appointmentId)
			throws ResourceNotFoundException {

		AppointmentDto appointmentDto = appointmentService.findAppointmentByAppointmentId(appointmentId);

		return new ResponseEntity<>(appointmentDto, HttpStatus.OK);

	}

	/**
	 * This method is used to update an appointment.
	 * 
	 * @param appointmentDto This is the appointment data to be updated.
	 * @return ResponseEntity This returns the updated appointment.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   appointment to be updated is not found.
	 */
	@PutMapping("/update")
	public ResponseEntity<Object> updateAppointment(@RequestBody AppointmentDto appointmentDto)
			throws ResourceNotFoundException {

		AppointmentDto appointment2 = appointmentService.updateAppointment(appointmentDto);
		return new ResponseEntity<>(appointment2, HttpStatus.OK);

	}

	/**
	 * This method is used to delete an appointment by its ID.
	 * 
	 * @param appointmentId This is the ID of the appointment to be deleted.
	 * @return ResponseEntity This returns a success message after deletion.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   appointment to be deleted is not found.
	 */
	@DeleteMapping("/delete/{appointmentId}")
	public ResponseEntity<Object> deleteAppointment(@PathVariable("appointmentId") long appointmentId)
			throws ResourceNotFoundException {

		appointmentService.deleteAppointment(appointmentId);
		return new ResponseEntity<>("Appointment Successfully Deleted", HttpStatus.OK);

	}
	
	
	@GetMapping("findAll/customer/{customerId}")
	public ResponseEntity<List<AppointmentDto>> getAllAppointmentForCustomer(@PathVariable("customerId") long customerId,
			@RequestParam(value = "shopId", required = false, defaultValue = "0") long shopId,
			@RequestParam(value = "categoryId", required = false, defaultValue = "0") long categoryId,
			@RequestParam(value = "date", required = false) LocalDate date
			) throws ResourceNotFoundException{
		List<AppointmentDto> appointmentDtos = null;
		if (shopId != 0) {
			appointmentDtos = appointmentService.findAllByCustomerIddAndShopId(customerId,shopId);
		} else if (categoryId != 0) {
			appointmentDtos = appointmentService.findAllByCustomerIdAndCategoryId(customerId, categoryId);
		} else if (date != null) {
			appointmentDtos = appointmentService.findAllCustomerIdAndAppointmentDate(customerId,date);
		} else {
			appointmentDtos = appointmentService.findAllByCustomerId(customerId);
		}
		
		return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
	}
	
	
	@GetMapping("findAll/tailor/{shopId}")
	public ResponseEntity<List<AppointmentDto>> getAllAppointmentForTailor(@PathVariable("shopId") long shopId,
			@RequestParam(value = "customerId", required = false, defaultValue = "0") long customerId,
			@RequestParam(value = "categoryId", required = false, defaultValue = "0") long categoryId,
			@RequestParam(value = "date", required = false) LocalDate date
			) throws ResourceNotFoundException{
		List<AppointmentDto> appointmentDtos = null;
		if (customerId != 0) {
			appointmentDtos = appointmentService.findAllByCustomerIddAndShopId(customerId,shopId);
		} else if (categoryId != 0) {
			appointmentDtos = appointmentService.findAllByShopIdAndCategoryId(shopId, categoryId);
		} else if (date != null) {
			appointmentDtos = appointmentService.findAllByShopIdAndAppointmentDate(shopId,date);
		} else {
			appointmentDtos = appointmentService.findAllByShopId(shopId);
		}
		
		return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
	}
	
	
	
	

}