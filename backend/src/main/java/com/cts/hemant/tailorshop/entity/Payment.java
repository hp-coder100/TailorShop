package com.cts.hemant.tailorshop.entity;

import java.time.LocalDate;

import com.cts.hemant.tailorshop.payload.PaymentDto;

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
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private long paymentId;
	
	@Column(name = "amount", nullable = false)
	private double amount;
	@Column(name = "payment_date", nullable = false)
	private LocalDate paymentDate;
	@Column(name = "status", nullable = false)
	private String status;
	
	@ManyToOne
	@JoinColumn(name="appointment_id")
	private Appointment appointment;
	
	
	public PaymentDto mapToPaymentDto() {
		return new PaymentDto(paymentId, amount, paymentDate, status, appointment.getAppointmentId());
	}

}
