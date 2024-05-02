package com.cts.hemant.tailorshop.service;

import java.util.List;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.CustomerDto;

public interface CustomerService {

	public CustomerDto createCustomer(CustomerDto customerDto) throws ResourceNotFoundException;

	public CustomerDto updateCustomer(CustomerDto customerDto) throws ResourceNotFoundException;

	public CustomerDto findByCustomerId(long customerId) throws ResourceNotFoundException;

	public void deleteCustomer(long customerId) throws ResourceNotFoundException;

	public CustomerDto findByEmailId(String email) throws ResourceNotFoundException;

	public List<CustomerDto> findAllCustomers();
}
