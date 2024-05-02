package com.cts.hemant.tailorshop.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.hemant.tailorshop.entity.Appointment;
import com.cts.hemant.tailorshop.entity.Category;
import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.AppointmentDto;
import com.cts.hemant.tailorshop.repository.AppointmentRepository;
import com.cts.hemant.tailorshop.repository.CategoryRepository;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.service.AppointmentService;
import com.cts.hemant.tailorshop.util.StaticStringValues;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TailorRepository tailorRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * Creates a new appointment based on the provided AppointmentDto.
	 * 
	 * @param appointmentDto Data transfer object containing appointment details.
	 * @return The created AppointmentDto with persisted data.
	 * @throws ResourceNotFoundException if the appointment already exists or
	 *                                   related entities are not found.
	 */
	@Override
	public AppointmentDto createAppointment(AppointmentDto appointmentDto) throws ResourceNotFoundException {
		// Log the creation request
		log.info(StaticStringValues.REQUESTING_APPOINTMENT_CREATE);

		// Check for existing appointment with the same ID
		if (appointmentRepository.findById(appointmentDto.getAppointmentId()).isPresent()) {
			throw new ResourceNotFoundException(
					StaticStringValues.APPOINTMENT_ALREADY_EXIST + appointmentDto.getAppointmentId());
		}

		// Validate customer existence
		Optional<Customer> customer = customerRepository.findById(appointmentDto.getCustomerId());
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + appointmentDto.getCustomerId());
		}

		// Validate tailor existence
		Optional<Tailor> tailor = tailorRepository.findById(appointmentDto.getShopId());
		if (tailor.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + appointmentDto.getShopId());
		}

		// Validate category existence
		Optional<Category> category = categoryRepository.findById(appointmentDto.getCategoryId());
		if (category.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING + appointmentDto.getShopId());
		}

		// Map DTO to entity and set relationships
		Appointment appointment = appointmentDto.mapToAppointment();
		appointment.setCustomer(customer.get());
		appointment.setTailor(tailor.get());
		appointment.setCategory(category.get());

		// Save the appointment and map back to DTO
		AppointmentDto createdAppointment = appointmentRepository.save(appointment).mapToAppointmentDto();

		// Log the successful creation
		log.debug(StaticStringValues.APPOINTMENT_CREATED_SUCCESSFULLY + " " + createdAppointment);
		log.info(StaticStringValues.APPOINTMENT_CREATED_SUCCESSFULLY + " " + createdAppointment);
		return createdAppointment;
	}

	/**
	 * Finds an appointment by its ID.
	 * 
	 * @param appointmentId The ID of the appointment to find.
	 * @return The found AppointmentDto.
	 * @throws ResourceNotFoundException if the appointment is not found.
	 */
	@Override
	public AppointmentDto findAppointmentByAppointmentId(long appointmentId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_APPOINTMENT_FINDBY_ID + appointmentId);

		// Attempt to retrieve the appointment
		Optional<Appointment> result = appointmentRepository.findById(appointmentId);

		// Check if the appointment is present
		if (result.isPresent()) {
			// Map the entity to DTO
			AppointmentDto appointmentDto = result.get().mapToAppointmentDto();

			// Log the successful find
			log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + appointmentDto);
			log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + appointmentDto);
			return appointmentDto;
		} else {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + appointmentId);
		}
	}

	/**
	 * Deletes an appointment by its ID.
	 * 
	 * @param appointmentId The ID of the appointment to delete.
	 * @throws ResourceNotFoundException if the appointment is not found.
	 */
	@Override
	public void deleteAppointment(long appointmentId) throws ResourceNotFoundException {
		// Log the delete request
		log.info(StaticStringValues.REQUEST_APPOINTMENT_DELETEBY_ID + appointmentId);

		// Check if the appointment exists
		Optional<Appointment> resultOptional = appointmentRepository.findById(appointmentId);
		if (resultOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + appointmentId);
		}

		// Delete the appointment
		appointmentRepository.delete(resultOptional.get());

		// Log the successful deletion
		log.info(StaticStringValues.APPOINTMET_DELETED_SUCCESSFULLY);
	}

	/**
	 * Updates an existing appointment based on the provided AppointmentDto.
	 * 
	 * @param appointmentDto Data transfer object containing appointment details.
	 * @return The updated AppointmentDto with persisted data.
	 * @throws ResourceNotFoundException if the appointment does not exist.
	 */
	@Override
	public AppointmentDto updateAppointment(AppointmentDto appointmentDto) throws ResourceNotFoundException {
		// Log the update request
		log.info(StaticStringValues.REQUEST_APPOINTMENT_UPDATE);

		// Check for existing appointment with the same ID
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentDto.getAppointmentId());
		if (appointment.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + appointmentDto.getAppointmentId());
		}

		// Update details if provided
		if (appointmentDto.getStatus() != null)
			appointment.get().setStatus(appointmentDto.getStatus());
		if (appointmentDto.getAppointmentDate() != null)
			appointment.get().setAppointmentDate(appointmentDto.getAppointmentDate());

		// Save the updated appointment and map back to DTO
		AppointmentDto updatedAppointment = appointmentRepository.save(appointment.get()).mapToAppointmentDto();

		// Log the successful update
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + updatedAppointment);
		log.info(StaticStringValues.APPOINTMENT_UPDATED_SUCCESSFULLY);
		return updatedAppointment;
	}

	/**
	 * Finds all appointments.
	 * 
	 * @return A list of all AppointmentDto.
	 */
	@Override
	public List<AppointmentDto> findAllAppointments() {
		// Log the find all request
		log.info(StaticStringValues.REQUEST_FIND_ALL_APPOINTMENTS);

		// Retrieve all appointments, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository.findAll().stream().map(app -> app.mapToAppointmentDto())
				.collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	/**
	 * Finds all appointments for a specific customer ID.
	 * 
	 * @param customerId The ID of the customer whose appointments are to be found.
	 * @return A list of AppointmentDto for the specified customer.
	 * @throws ResourceNotFoundException if the customer does not exist.
	 */
	@Override
	public List<AppointmentDto> findAllByCustomerId(long customerId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_FIND_ALL_APPOINTMENTS_BY_CUSTOMER_ID + customerId);

		// Validate customer existence
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Retrieve all appointments for the customer, map to DTOs, and collect to a
		// list
		List<AppointmentDto> list = appointmentRepository.findAllByCustomer_CustomerId(customerId).stream()
				.map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	/**
	 * Finds all appointments for a specific shop ID.
	 * 
	 * @param shopId The ID of the shop whose appointments are to be found.
	 * @return A list of AppointmentDto for the specified shop.
	 * @throws ResourceNotFoundException if the shop does not exist.
	 */
	@Override
	public List<AppointmentDto> findAllByShopId(long shopId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_FIND_ALL_APPOINTMENTS_BY_SHOP_ID + shopId);

		// Validate shop existence
		Optional<Tailor> tailor = tailorRepository.findById(shopId);
		if (tailor.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}

		// Retrieve all appointments for the shop, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository.findAllByTailor_ShopId(shopId).stream()
				.map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	/**
	 * Finds all appointments for a specific date.
	 * 
	 * @param date The date of the appointments to be found.
	 * @return A list of AppointmentDto for the specified date.
	 */
	@Override
	public List<AppointmentDto> findAllByDate(LocalDate date) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_FIND_ALL_APPOINTMENTS_BY_DATE);

		// Retrieve all appointments for the date, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository.findAllByAppointmentDate(date).stream()
				.map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	/**
	 * Finds all appointments for a specific category ID.
	 * 
	 * @param categoryId The ID of the category whose appointments are to be found.
	 * @return A list of AppointmentDto for the specified category.
	 * @throws ResourceNotFoundException if the category does not exist.
	 */
	@Override
	public List<AppointmentDto> findAllByCategoryId(long categoryId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_FIND_ALL_APPOINTMENTS_BY_CATEGORY_ID);

		// Validate category existence
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING + categoryId);
		}

		// Retrieve all appointments for the category, map to DTOs, and collect to a
		// list
		List<AppointmentDto> list = appointmentRepository.findAllByCategory_CategoryId(categoryId).stream()
				.map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	@Override
	public List<AppointmentDto> findAllByCustomerIddAndShopId(long customerId, long shopId) throws ResourceNotFoundException {
		// Log the find request
		// Validate customer existence
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Validate shop existence
		Optional<Tailor> tailor = tailorRepository.findById(shopId);
		if (tailor.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}

		// Retrieve all appointments for the shop, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository
				.findAllByCustomer_CustomerIdAndTailor_ShopId(customerId, shopId).stream()
				.map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;

	}

	@Override
	public List<AppointmentDto> findAllCustomerIdAndAppointmentDate(long customerId, LocalDate date) throws ResourceNotFoundException {
		// Validate customer existence
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Retrieve all appointments for the shop, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository.findAllByCustomer_CustomerIdAndAppointmentDate(customerId, date)
				.stream().map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	@Override
	public List<AppointmentDto> findAllByCustomerIdAndStatus(long customerId, String status) throws ResourceNotFoundException {
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Retrieve all appointments for the shop, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository.findAllByCustomer_CustomerIdAndStatus(customerId, status)
				.stream().map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;

	}

	@Override
	public List<AppointmentDto> findAllByCustomerIdAndCategoryId(long customerId, long categoryId) throws ResourceNotFoundException {
		Optional<Customer> customer = customerRepository.findById(customerId);
		if (customer.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Validate category existence
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING + categoryId);
		}
		// Retrieve all appointments for the shop, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository
				.findAllByCustomer_CustomerIdAndCategory_CategoryId(customerId, categoryId).stream()
				.map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	@Override
	public List<AppointmentDto> findAllByShopIdAndAppointmentDate(long shopId, LocalDate date) throws ResourceNotFoundException {
		// Validate shop existence
		Optional<Tailor> tailor = tailorRepository.findById(shopId);
		if (tailor.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}
		// Retrieve all appointments for the shop, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository.findAllByTailor_ShopIdAndAppointmentDate(shopId, date)
				.stream().map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;
	}

	@Override
	public List<AppointmentDto> findAllByShopIdAndStatus(long shopId, String status) throws ResourceNotFoundException {

		// Validate shop existence
		Optional<Tailor> tailor = tailorRepository.findById(shopId);
		if (tailor.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}
		// Retrieve all appointments for the shop, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository.findAllByTailor_ShopIdAndStatus(shopId, status).stream()
				.map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;

	}

	@Override
	public List<AppointmentDto> findAllByShopIdAndCategoryId(long shopId, long categoryId) throws ResourceNotFoundException {

		// Validate shop existence
		Optional<Tailor> tailor = tailorRepository.findById(shopId);
		if (tailor.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}
		// Validate category existence
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isEmpty()) {
			throw new ResourceNotFoundException(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING + categoryId);
		}
		// Retrieve all appointments for the shop, map to DTOs, and collect to a list
		List<AppointmentDto> list = appointmentRepository
				.findAllByTailor_ShopIdAndCategory_CategoryId(shopId, categoryId).stream()
				.map(app -> app.mapToAppointmentDto()).collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.APPOINTMENT_FOUND_SUCCESSFULLY);
		return list;
	}

}
