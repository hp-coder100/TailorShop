package com.cts.hemant.tailorshop.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.hemant.tailorshop.entity.Appointment;
import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Payment;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.PaymentDto;
import com.cts.hemant.tailorshop.repository.AppointmentRepository;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.PaymentRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.service.PaymentService;
import com.cts.hemant.tailorshop.util.StaticStringValues;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TailorRepository tailorRepository;

	/**
	 * Creates a new payment record based on the provided PaymentDto.
	 *
	 * @param paymentDto Data transfer object containing payment details.
	 * @return The created PaymentDto with persisted data.
	 * @throws ResourceNotFoundException if the payment already exists or related
	 *                                   entities such as the appointment are not
	 *                                   found.
	 */
	@Override
	public PaymentDto createPayment(PaymentDto paymentDto) throws ResourceNotFoundException {
		log.info(StaticStringValues.REQUESTING_PAYMENT_CREATE); // Logging the start of the create payment request

		// Checking if the payment ID is valid
		Optional<Payment> paymentOptional = paymentRepository.findById(paymentDto.getPaymentId());
		if (paymentOptional.isPresent()) {
			// If the payment ID is already present, throwing a resource not found exception
			throw new ResourceNotFoundException(
					StaticStringValues.NOTIFICATION_ALREADY_EXIST + paymentDto.getPaymentId());
		}

		// Checking if the appointment ID is valid
		Optional<Appointment> appointmentOptional = appointmentRepository.findById(paymentDto.getAppointmentId());
		if (appointmentOptional.isEmpty()) {
			// If the appointment ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(
					StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + paymentDto.getAppointmentId());
		}

		// Mapping the payment DTO to a payment entity and setting the appointment
		Payment payment = paymentDto.mapToPayment();
		payment.setAppointment(appointmentOptional.get());

		// Saving the payment entity and mapping it back to a payment DTO
		PaymentDto paymentDto2 = paymentRepository.save(payment).mapToPaymentDto();
		log.debug(StaticStringValues.PAYMENT_CREATED_SUCCESSFULLY + " " + paymentDto2); // Debug logging the successful
																						// creation of the payment
		log.info(StaticStringValues.PAYMENT_CREATED_SUCCESSFULLY); // Logging the successful completion of the create
																	// payment request

		// Returning the created payment DTO
		return paymentDto2;
	}

	/**
	 * Deletes a payment record identified by the payment ID.
	 *
	 * @param paymentId The unique identifier of the payment to be deleted.
	 * @throws ResourceNotFoundException if the payment does not exist.
	 */
	@Override
	public void deletePayment(long paymentId) throws ResourceNotFoundException {
		log.info(StaticStringValues.REQUEST_PAYMENT_DELETEBY_ID + paymentId); // Logging the start of the delete payment
																				// request

		// Checking if the payment ID is valid
		Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
		if (paymentOptional.isEmpty()) {
			// If the payment ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.PAYMENT_DOES_NOT_EXIT_STRING + paymentId);
		}

		// Deleting the payment entity
		paymentRepository.delete(paymentOptional.get());
		log.info(StaticStringValues.PAYMENT_DELETED_SUCCESSFULLY); // Logging the successful completion of the delete
																	// payment request
	}

	/**
	 * Updates an existing payment record with the details provided in PaymentDto.
	 *
	 * @param paymentDto Data transfer object containing updated payment details.
	 * @return The updated PaymentDto with persisted changes.
	 * @throws ResourceNotFoundException if the payment to be updated does not
	 *                                   exist.
	 */
	@Override
	public PaymentDto updatePayment(PaymentDto paymentDto) throws ResourceNotFoundException {
		log.info(StaticStringValues.REQUEST_PAYMENT_UPDATE); // Logging the start of the update payment request

		// Checking if the payment ID is valid
		Optional<Payment> paymentOptional = paymentRepository.findById(paymentDto.getPaymentId());
		if (paymentOptional.isEmpty()) {
			// If the payment ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(
					StaticStringValues.PAYMENT_DOES_NOT_EXIT_STRING + paymentDto.getPaymentId());
		}

		// Getting the payment entity
		Payment payment = paymentOptional.get();

		// Updating the status of the payment if it is present in the payment DTO
		if (paymentDto.getStatus() != null)
			payment.setStatus(paymentDto.getStatus());

		// Saving the updated payment entity and mapping it back to a payment DTO
		PaymentDto updatedDto = paymentRepository.save(payment).mapToPaymentDto();
		log.debug(StaticStringValues.PAYMENT_UPDATED_SUCCESSFULLY + " " + updatedDto); // Debug logging the successful
																						// update of the payment
		log.info(StaticStringValues.PAYMENT_UPDATED_SUCCESSFULLY); // Logging the successful completion of the update
																	// payment request

		// Returning the updated payment DTO
		return updatedDto;
	}

	/**
	 * Retrieves all payment records associated with a given appointment ID.
	 *
	 * @param appointmentId The unique identifier of the appointment.
	 * @return A list of PaymentDto objects associated with the appointment ID.
	 * @throws ResourceNotFoundException if the appointment does not exist.
	 */
	@Override
	public List<PaymentDto> findAllByAppointmentId(long appointmentId) throws ResourceNotFoundException {
		log.info(StaticStringValues.REQUEST_FIND_ALL_PAYMENTS_BY_APPOINTMENT_ID + appointmentId); // Logging the start
																									// of the find all
																									// payments by
																									// appointment ID
																									// request

		// Checking if the appointment ID is valid
		Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
		if (appointmentOptional.isEmpty()) {
			// If the appointment ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.PAYMENT_DOES_NOT_EXIT_STRING + appointmentId);
		}

		// Fetching all payments for the given appointment ID from the repository,
		// mapping them to DTOs, and collecting them into a list
		List<PaymentDto> paymentDto = paymentRepository.findAllByAppointment_AppointmentId(appointmentId).stream()
				.map(pay -> pay.mapToPaymentDto()).collect(Collectors.toList());
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto); // Debug logging the successful
																						// finding of payments
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY); // Logging the successful completion of the find all
																	// payments by appointment ID request

		// Returning the list of all payments for the given appointment ID
		return paymentDto;
	}

	/**
	 * Retrieves a payment record by its unique identifier.
	 *
	 * @param paymentId The unique identifier of the payment to be found.
	 * @return The PaymentDto of the found payment.
	 * @throws ResourceNotFoundException if the payment does not exist.
	 */
	@Override
	public PaymentDto findByPaymentId(long paymentId) throws ResourceNotFoundException {
		// Logging the start of the find payment by ID request
		log.info(StaticStringValues.REQUEST_PAYMENT_FINDBY_ID + paymentId);

		// Checking if the payment ID is valid
		Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
		if (paymentOptional.isEmpty()) {
			// If the payment ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.PAYMENT_DOES_NOT_EXIT_STRING + paymentId);
		}
		// Logging the successful finding of the payment
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentOptional.get());
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning the PaymentDto of the found payment
		return paymentOptional.get().mapToPaymentDto();
	}

	/**
	 * Retrieves all payment records.
	 *
	 * @return A list of PaymentDto objects representing all payments.
	 */
	@Override
	public List<PaymentDto> findAllPayments() {
		// Logging the start of the find all payments request
		log.info(StaticStringValues.REQUEST_FIND_ALL_PAYMENTS);

		// Fetching all payments from the repository, mapping them to DTOs, and
		// collecting them into a list
		List<PaymentDto> paymentDto = paymentRepository.findAll().stream().map(pay -> pay.mapToPaymentDto())
				.collect(Collectors.toList());

		// Logging the successful finding of payments
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto);
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning a list of PaymentDto objects representing all payments
		return paymentDto;
	}

	/**
	 * Retrieves all payment records with a specific status.
	 *
	 * @param status The status of the payments to be found.
	 * @return A list of PaymentDto objects with the specified status.
	 * @throws ResourceNotFoundException if no payments with the specified status
	 *                                   are found.
	 */
	@Override
	public List<PaymentDto> findAllByStatus(String status) throws ResourceNotFoundException {
		// Logging the start of the find all payments by status request
		log.info(StaticStringValues.REQUEST_FIND_ALL_PAYMENTS_BY_STATUS + status);

		// Fetching all payments with the specified status from the repository, mapping
		// them to DTOs, and collecting them into a list
		List<PaymentDto> paymentDto = paymentRepository.findAllByStatus(status).stream()
				.map(pay -> pay.mapToPaymentDto()).collect(Collectors.toList());

		// Logging the successful finding of payments
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto);
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning a list of PaymentDto objects with the specified status
		return paymentDto;
	}

	/**
	 * Retrieves all payment records made on a specific date.
	 *
	 * @param date The date of the payments to be found.
	 * @return A list of PaymentDto objects made on the specified date.
	 * @throws ResourceNotFoundException if no payments made on the specified date
	 *                                   are found.
	 */
	@Override
	public List<PaymentDto> findAllByDate(LocalDate date) throws ResourceNotFoundException {
		// Logging the start of the find all payments by date request
		log.info(StaticStringValues.REQUEST_FIND_ALL_PAYMENTS_BY_DATE + date);

		// Fetching all payments made on the specified date from the repository, mapping
		// them to DTOs, and collecting them into a list
		List<PaymentDto> paymentDto = paymentRepository.findAllByPaymentDate(date).stream()
				.map(pay -> pay.mapToPaymentDto()).collect(Collectors.toList());

		// Logging the successful finding of payments
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto);
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning a list of PaymentDto objects made on the specified date
		return paymentDto;
	}

	@Override
	public List<PaymentDto> findAllByCustomerId(long customerId) throws ResourceNotFoundException {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		if (customerOptional.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}
		List<PaymentDto> paymentDto = paymentRepository.findAllByAppointment_Customer_CustomerId(customerId).stream()
				.map(pay -> pay.mapToPaymentDto()).collect(Collectors.toList());

		// Logging the successful finding of payments
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto);
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning a list of PaymentDto objects made on the specified date
		return paymentDto;
	}

	@Override
	public List<PaymentDto> findAllByCustomerIdAndStatus(long customerId, String status) throws ResourceNotFoundException {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		if (customerOptional.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}
		List<PaymentDto> paymentDto = paymentRepository.findAllByAppointment_Customer_CustomerIdAndStatus(customerId,status).stream()
				.map(pay -> pay.mapToPaymentDto()).collect(Collectors.toList());

		// Logging the successful finding of payments
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto);
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning a list of PaymentDto objects made on the specified date
		return paymentDto;
	}

	@Override
	public List<PaymentDto> findAllByCustomerIdAndPaymentDate(long customerId, LocalDate date) throws ResourceNotFoundException {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		if (customerOptional.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}
		List<PaymentDto> paymentDto = paymentRepository.findAllByAppointment_Customer_CustomerIdAndPaymentDate(customerId,date).stream()
				.map(pay -> pay.mapToPaymentDto()).collect(Collectors.toList());

		// Logging the successful finding of payments
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto);
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning a list of PaymentDto objects made on the specified date
		return paymentDto;
	}

	@Override
	public List<PaymentDto> findAllByShopId(long shopId) throws ResourceNotFoundException {
		Optional<Tailor> tailorOptional = tailorRepository.findById(shopId);
		if (tailorOptional.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}
		List<PaymentDto> paymentDto = paymentRepository.findAllByAppointment_Tailor_ShopId(shopId).stream()
				.map(pay -> pay.mapToPaymentDto()).collect(Collectors.toList());

		// Logging the successful finding of payments
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto);
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning a list of PaymentDto objects made on the specified date
		return paymentDto;
	}

	@Override
	public List<PaymentDto> findAllByShopIdAndStatus(long shopId, String status) throws ResourceNotFoundException {
		Optional<Tailor> tailorOptional = tailorRepository.findById(shopId);
		if (tailorOptional.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}
		List<PaymentDto> paymentDto = paymentRepository.findAllByAppointment_Tailor_ShopIdAndStatus(shopId,status).stream()
				.map(pay -> pay.mapToPaymentDto()).collect(Collectors.toList());

		// Logging the successful finding of payments
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto);
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning a list of PaymentDto objects made on the specified date
		return paymentDto;
	}

	@Override
	public List<PaymentDto> findAllByShopIdAndPaymentDate(long shopId, LocalDate date) throws ResourceNotFoundException {
		Optional<Tailor> tailorOptional = tailorRepository.findById(shopId);
		if (tailorOptional.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}
		List<PaymentDto> paymentDto = paymentRepository.findAllByAppointment_Tailor_ShopIdAndPaymentDate(shopId,date).stream()
				.map(pay -> pay.mapToPaymentDto()).collect(Collectors.toList());

		// Logging the successful finding of payments
		log.debug(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY + " " + paymentDto);
		log.info(StaticStringValues.PAYMENT_FOUND_SUCCESSFULLY);

		// Returning a list of PaymentDto objects made on the specified date
		return paymentDto;
	}

}
