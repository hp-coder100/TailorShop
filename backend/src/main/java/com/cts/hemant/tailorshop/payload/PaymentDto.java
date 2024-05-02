package com.cts.hemant.tailorshop.payload;

import java.time.LocalDate;

import com.cts.hemant.tailorshop.entity.Payment;

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
public class PaymentDto {

	private long paymentId;

	private double amount;
	private LocalDate paymentDate;
	private String status;

	private long appointmentId;

	public Payment mapToPayment() {
		return new Payment(paymentId, amount, paymentDate, status, null);
	}
}
