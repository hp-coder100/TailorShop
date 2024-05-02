package com.cts.hemant.tailorshop.entity;

import java.time.LocalDate;

import com.cts.hemant.tailorshop.payload.AppointmentDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long appointmentId;

	@Column(nullable = false)
	private LocalDate appointmentDate;
	
	@Column(nullable = false)
	private String status;

	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="shop_id")
	private Tailor tailor;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	
	public AppointmentDto mapToAppointmentDto() {
		
		AppointmentDto appointmentDto = new AppointmentDto(appointmentId, appointmentDate, status, customer.getCustomerId(), tailor.getShopId(), category.getCategoryId());
		
		return appointmentDto;
		
	}
}
