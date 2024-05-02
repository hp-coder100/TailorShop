package com.cts.hemant.tailorshop.controller;

import java.time.LocalDate;

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
import com.cts.hemant.tailorshop.payload.PaymentDto;
import com.cts.hemant.tailorshop.service.PaymentService;

/**
 * Controller class for handling payment-related operations.
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	/**
	 * This method is used to create a new payment.
	 * 
	 * @param paymentDto This is the payment data to be created.
	 * @return ResponseEntity This returns the created payment.
	 * @throws ResourceNotFoundException This exception is thrown when the payment
	 *                                   to be created is not found.
	 */
	@PostMapping("/create")
	public ResponseEntity<Object> createPayment(@RequestBody PaymentDto paymentDto) throws ResourceNotFoundException {
		PaymentDto paymentDto2 = paymentService.createPayment(paymentDto);
		return new ResponseEntity<>(paymentDto2, HttpStatus.CREATED);
	}

	/**
	 * This method is used to get all payments or filter payments by appointment ID,
	 * status, or date.
	 * 
	 * @param appointmentId This is the ID of the appointment whose payments are to
	 *                      be found. If not provided, all payments are found.
	 * @param status        This is the status of the payments to be found. If not
	 *                      provided, all payments are found.
	 * @param date          This is the date of the payments to be found. If not
	 *                      provided, all payments are found.
	 * @return ResponseEntity This returns the list of found payments.
	 * @throws ResourceNotFoundException This exception is thrown when the payments
	 *                                   are not found.
	 */
	@GetMapping("/findAll")
	public ResponseEntity<Object> findAllPayments(
			@RequestParam(value = "appointmentId", required = false, defaultValue = "0") long appointmentId,
			@RequestParam(value = "status", required = false, defaultValue = "NotRequired") String status,
			@RequestParam(value = "date", required = false) LocalDate date) throws ResourceNotFoundException {
		Object paymentDtoDtos = null;

		if (appointmentId != 0) {
			paymentDtoDtos = paymentService.findAllByAppointmentId(appointmentId);
		} else if (!status.equals("NotRequired")) {
			paymentDtoDtos = paymentService.findAllByStatus(status);
		} else if (date != null) {
			paymentDtoDtos = paymentService.findAllByDate(date);
		} else {
			paymentDtoDtos = paymentService.findAllPayments();
		}
		return new ResponseEntity<>(paymentDtoDtos, HttpStatus.OK);
	}
	
	
	@GetMapping("/findAll/customer/{customerId}")
	public ResponseEntity<Object> findAllPaymentsForCustomer(@PathVariable("customerId") long customerId,
			@RequestParam(value = "status", required = false, defaultValue = "NotRequired") String status,
			@RequestParam(value = "date", required = false) LocalDate date) throws ResourceNotFoundException {
		Object paymentDtoDtos = null;

		if (!status.equals("NotRequired")) {
			paymentDtoDtos = paymentService.findAllByCustomerIdAndStatus(customerId,status);
		} else if (date != null) {
			paymentDtoDtos = paymentService.findAllByCustomerIdAndPaymentDate(customerId,date);
		} else {
			paymentDtoDtos = paymentService.findAllByCustomerId(customerId);
		}
		return new ResponseEntity<>(paymentDtoDtos, HttpStatus.OK);
	}
	
	
	@GetMapping("/findAll/tailor/{shopId}")
	public ResponseEntity<Object> findAllPaymentsForTailor(@PathVariable("shopId") long shopId,
			@RequestParam(value = "status", required = false, defaultValue = "NotRequired") String status,
			@RequestParam(value = "date", required = false) LocalDate date) throws ResourceNotFoundException {
		Object paymentDtoDtos = null;

		if (!status.equals("NotRequired")) {
			paymentDtoDtos = paymentService.findAllByShopIdAndStatus(shopId,status);
		} else if (date != null) {
			paymentDtoDtos = paymentService.findAllByShopIdAndPaymentDate(shopId,date);
		} else {
			paymentDtoDtos = paymentService.findAllByShopId(shopId);
		}
		return new ResponseEntity<>(paymentDtoDtos, HttpStatus.OK);
	}

	/**
	 * This method is used to update a payment.
	 * 
	 * @param paymentDto This is the payment data to be updated.
	 * @return ResponseEntity This returns the updated payment.
	 * @throws ResourceNotFoundException This exception is thrown when the payment
	 *                                   to be updated is not found.
	 */
	@PutMapping("/update")
	public ResponseEntity<Object> updatePayment(@RequestBody PaymentDto paymentDto) throws ResourceNotFoundException {
		PaymentDto paymentDto2 = paymentService.updatePayment(paymentDto);
		return new ResponseEntity<>(paymentDto2, HttpStatus.OK);
	}

	/**
	 * This method is used to get a payment by its ID.
	 * 
	 * @param paymentId This is the ID of the payment to be found.
	 * @return ResponseEntity This returns the found payment.
	 * @throws ResourceNotFoundException This exception is thrown when the payment
	 *                                   is not found.
	 */
	@GetMapping("/find/{paymentId}")
	public ResponseEntity<Object> findByNotificationId(@PathVariable("paymentId") long paymentId)
			throws ResourceNotFoundException {
		PaymentDto paymentDtoDto = paymentService.findByPaymentId(paymentId);
		return new ResponseEntity<>(paymentDtoDto, HttpStatus.OK);
	}

	/**
	 * This method is used to delete a payment by its ID.
	 * 
	 * @param paymentId This is the ID of the payment to be deleted.
	 * @return ResponseEntity This returns a success message after deletion.
	 * @throws ResourceNotFoundException This exception is thrown when the payment
	 *                                   to be deleted is not found.
	 */
	@DeleteMapping("/delete/{paymentId}")
	public ResponseEntity<Object> deleteNotification(@PathVariable("paymentId") long paymentId)
			throws ResourceNotFoundException {
		paymentService.deletePayment(paymentId);
		return new ResponseEntity<>("Payment deleted Successfully", HttpStatus.OK);
	}
	
	
	
	
}
