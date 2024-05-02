package com.cts.hemant.tailorshop.entity;

import com.cts.hemant.tailorshop.payload.MeasurementDto;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Measurement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long measurementId;
	
	@Column(nullable = false)
	private String details;
	
	
	@ManyToOne
	@JoinColumn(name="appointment_id")
	private Appointment appointment;
	
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "shop_id")
	private Tailor tailor;
	
	public MeasurementDto mapToMeasurementDto() {
		return new MeasurementDto(measurementId, details, appointment.getAppointmentId(), customer.getCustomerId(), tailor.getShopId());
	}
}
