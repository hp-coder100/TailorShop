package com.cts.hemant.tailorshop.entity;

import com.cts.hemant.tailorshop.payload.CustomerDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private long customerId;
	@Column(name = "customer_firstname", nullable = false)
	private String customerFirstName;
	@Column(name = "customer_lastname", nullable = false)
	private String customerLastName;
	@Column(nullable = false)
	private String address;

	private long phone;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;

	public CustomerDto mapToCustomerDto() {
		return new CustomerDto(customerId, customerFirstName, customerLastName, address, phone, email, null);
	}

}
