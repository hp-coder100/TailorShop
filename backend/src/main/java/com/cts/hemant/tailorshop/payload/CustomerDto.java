package com.cts.hemant.tailorshop.payload;


import com.cts.hemant.tailorshop.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

	private long customerId;
	private String customerFirstName;
	private String customerLastName;
	private String address;
	private long phone;
	private String email;
	private String password;
	
	public Customer mapToCustomer() {
		return new Customer(customerId, customerFirstName, customerLastName, address, phone, email, password);
	}
}
