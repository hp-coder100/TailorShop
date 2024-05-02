package com.cts.hemant.tailorshop.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.CustomerDto;
import com.cts.hemant.tailorshop.service.CustomerService;

/**
 * Controller class for handling customer-related operations.
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	

	/**
	 * This method is used to create a new customer.
	 * 
	 * @param customerDto This is the customer data to be created.
	 * @return ResponseEntity This returns the created customer.
	 * @throws ResourceNotFoundException This exception is thrown when the customer
	 *                                   to be created is not found.
	 */
	@PostMapping("/create")
	public ResponseEntity<Object> createCustomer(@RequestBody CustomerDto customerDto)
			throws ResourceNotFoundException {
		CustomerDto customerDto2 = customerService.createCustomer(customerDto);
		return new ResponseEntity<>(customerDto2, HttpStatus.CREATED);
	}

	/**
	 * This method is used to find customers by email or find all customers if no
	 * email is provided.
	 * 
	 * @param email This is the email of the customer to be found. If not provided,
	 *              all customers are found.
	 * @return ResponseEntity This returns the found customer(s).
	 * @throws ResourceNotFoundException This exception is thrown when the customers
	 *                                   are not found.
	 */
	@GetMapping("/findAll")
	public ResponseEntity<Object> findCustomers(
			)
			throws ResourceNotFoundException {

			List<CustomerDto> customerDtos = customerService.findAllCustomers();
			return new ResponseEntity<>(customerDtos, HttpStatus.OK);
		
	}

	/**
	 * This method is used to update a customer.
	 * 
	 * @param customerDto This is the customer data to be updated.
	 * @return ResponseEntity This returns the updated customer.
	 * @throws ResourceNotFoundException This exception is thrown when the customer
	 *                                   to be updated is not found.
	 */
	@PutMapping("/update")
	public ResponseEntity<Object> updateCustomer(@RequestBody CustomerDto customerDto)
			throws ResourceNotFoundException {
		CustomerDto customerDto2 = customerService.updateCustomer(customerDto);
		return new ResponseEntity<>(customerDto2, HttpStatus.OK);
	}

	/**
	 * This method is used to delete a customer by its ID.
	 * 
	 * @param customerId This is the ID of the customer to be deleted.
	 * @return ResponseEntity This returns a success message after deletion.
	 * @throws ResourceNotFoundException This exception is thrown when the customer
	 *                                   to be deleted is not found.
	 */
	@DeleteMapping("/delete/{customerId}")
	public ResponseEntity<Object> deleteCustomer(@PathVariable("customerId") long customerId)
			throws ResourceNotFoundException {
		customerService.deleteCustomer(customerId);
		return new ResponseEntity<>("Customer deleted Successfully", HttpStatus.OK);
	}

	/**
	 * This method is used to find a customer by its ID.
	 * 
	 * @param customerId This is the ID of the customer to be found.
	 * @return ResponseEntity This returns the found customer.
	 * @throws ResourceNotFoundException This exception is thrown when the customer
	 *                                   is not found.
	 */
	@GetMapping("/find/{customerId}")
	public ResponseEntity<Object> findCustomerById(@PathVariable("customerId") long customerId)
			throws ResourceNotFoundException {
		CustomerDto customerDto = customerService.findByCustomerId(customerId);
		return new ResponseEntity<>(customerDto, HttpStatus.OK);
	}

}
