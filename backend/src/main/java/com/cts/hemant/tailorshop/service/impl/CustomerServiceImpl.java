package com.cts.hemant.tailorshop.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.CustomerDto;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.service.CustomerService;
import com.cts.hemant.tailorshop.util.StaticStringValues;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private TailorRepository tailorRepository;

	@Autowired 
	private PasswordEncoder passwordEncoder;
	/**
	 * Creates a new customer based on the provided CustomerDto.
	 * 
	 * @param customerDto Data transfer object containing customer details.
	 * @return The created CustomerDto with persisted data.
	 * @throws ResourceNotFoundException if the customer already exists.
	 */
	@Override
	public CustomerDto createCustomer(CustomerDto customerDto) throws ResourceNotFoundException {
		// Log the creation request
		log.info(StaticStringValues.REQUESTING_CUSTOMER_CREATE);

		// Check for existing customer with the same ID
		if (customerRepository.findById(customerDto.getCustomerId()).isPresent()) {
			throw new ResourceNotFoundException(
					StaticStringValues.CUSTOMER_ALREADY_EXIST + customerDto.getCustomerId());
		}
		// Check for existing customer with the same email
		if (customerRepository.findByEmail(customerDto.getEmail()).isPresent()) {
			throw new ResourceNotFoundException(
					StaticStringValues.EMAIL_ALREADY_REGISTERED_AS_CUSTOMER + customerDto.getEmail());
		}


		// Checking if the tailor ID is valid
		Optional<Tailor> tailorOptional2 = tailorRepository.findByEmail(customerDto.getEmail());
		if (tailorOptional2.isPresent()) {
			// If the tailor ID is already present, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.EMAIL_ALREADY_REGISTERED_AS_TAILOR);
		}
		// Map DTO to entity
		//encryption and hashing of password
		 String hashPwd = passwordEncoder.encode(customerDto.getPassword());
		 customerDto.setPassword(hashPwd);
		 Customer customer = customerDto.mapToCustomer();

		// Log the created customer
		

		// Save the customer and map back to DTO
		CustomerDto savedCustomerDto = customerRepository.save(customer).mapToCustomerDto();

		// Log the successful creation
		log.debug(StaticStringValues.CUSTOMER_CREATED_SUCCESSFULLY + " " + customer.toString());
		log.info(StaticStringValues.CUSTOMER_CREATED_SUCCESSFULLY);
		return savedCustomerDto;
	}

	/**
	 * Updates an existing customer based on the provided CustomerDto.
	 * 
	 * @param customerDto Data transfer object containing customer details.
	 * @return The updated CustomerDto with persisted data.
	 * @throws ResourceNotFoundException if the customer does not exist.
	 */
	@Override
	public CustomerDto updateCustomer(CustomerDto customerDto) throws ResourceNotFoundException {
		// Log the update request
		log.info(StaticStringValues.REQUEST_CUSTOMER_UPDATE);

		// Check for existing customer with the same ID
		Optional<Customer> customerOptional = customerRepository.findById(customerDto.getCustomerId());
		if (customerOptional.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerDto.getCustomerId());
		}

		// Update details if provided
		Customer customer = customerOptional.get();
		if (customerDto.getAddress() != null) {
			customer.setAddress(customerDto.getAddress());
		}
//		if (customerDto.getCustomerFirstName() != null) {
//			customer.setCustomerFirstName(customerDto.getCustomerFirstName());
//		}
//		if (customerDto.getCustomerLastName() != null) {
//			customer.setCustomerLastName(customerDto.getCustomerLastName());
//		}
//		if (customerDto.getEmail() != null) {
//			customer.setEmail(customerDto.getEmail());
//		}
		if (customerDto.getPhone() != 0) {
			customer.setPhone(customerDto.getPhone());
		}
//		if (customerDto.getPassword() != null) {
//			customer.setPassword(customerDto.getPassword());
//		}

		// Save the updated customer and map back to DTO
		CustomerDto updatedCustomerDto = customerRepository.save(customer).mapToCustomerDto();

		// Log the successful update
		log.debug(StaticStringValues.CUSTOMER_FOUND_SUCCESSFULLY + " " + updatedCustomerDto);
		log.info(StaticStringValues.CUSTOMER_UPDATED_SUCCESSFULLY);
		return updatedCustomerDto;
	}

	/**
	 * Finds a customer by its ID.
	 * 
	 * @param customerId The ID of the customer to find.
	 * @return The found CustomerDto.
	 * @throws ResourceNotFoundException if the customer is not found.
	 */
	@Override
	public CustomerDto findByCustomerId(long customerId) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_CUSTOMER_FINDBY_ID + customerId);

		// Attempt to retrieve the customer
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		if (customerOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Map the entity to DTO
		CustomerDto customerDto = customerOptional.get().mapToCustomerDto();

		// Log the successful find
		log.debug(StaticStringValues.CUSTOMER_FOUND_SUCCESSFULLY + " " + customerDto);
		log.info(StaticStringValues.CUSTOMER_FOUND_SUCCESSFULLY);
		return customerDto;
	}

	/**
	 * Deletes a customer by its ID.
	 * 
	 * @param customerId The ID of the customer to delete.
	 * @throws ResourceNotFoundException if the customer is not found.
	 */
	@Override
	public void deleteCustomer(long customerId) throws ResourceNotFoundException {
		// Log the delete request
		log.info(StaticStringValues.REQUEST_CUSTOMER_DELETEBY_ID + customerId);

		// Check if the customer exists
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		if (customerOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Delete the customer
		customerRepository.delete(customerOptional.get());

		// Log the successful deletion
		log.info(StaticStringValues.CUSTOMER_DELETED_SUCCESSFULLY);
	}

	/**
	 * Finds a customer by its email ID.
	 * 
	 * @param email The email ID of the customer to find.
	 * @return The found CustomerDto.
	 * @throws ResourceNotFoundException if the customer is not found.
	 */
	@Override
	public CustomerDto findByEmailId(String email) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_CUSTOMER_FINDBY_EMAIL + email);

		// Attempt to retrieve the customer
		Optional<Customer> customerOptional = customerRepository.findByEmail(email);
		if (customerOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + email);
		}

		// Log the found customer
		log.debug(StaticStringValues.CUSTOMER_FOUND_SUCCESSFULLY + " " + customerOptional.get());

		// Map the entity to DTO
		CustomerDto customerDto = customerOptional.get().mapToCustomerDto();

		// Log the successful find
		log.info(StaticStringValues.CUSTOMER_FOUND_SUCCESSFULLY);
		return customerDto;
	}

	/**
	 * Finds all customers.
	 * 
	 * @return A list of all CustomerDto.
	 */
	@Override
	public List<CustomerDto> findAllCustomers() {
		// Log the find all request
		log.info(StaticStringValues.REQUEST_FIND_ALL_CUSTOMERS);

		// Retrieve all customers, map to DTOs, and collect to a list
		List<CustomerDto> list = customerRepository.findAll().stream().map(cust -> cust.mapToCustomerDto())
				.collect(Collectors.toList());

		// Log the successful find
		log.debug(StaticStringValues.CUSTOMER_FOUND_SUCCESSFULLY + " " + list);
		log.info(StaticStringValues.CUSTOMER_FOUND_SUCCESSFULLY);
		return list;
	}

}
