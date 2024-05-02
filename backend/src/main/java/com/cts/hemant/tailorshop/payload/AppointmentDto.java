package com.cts.hemant.tailorshop.payload;

import java.time.LocalDate;

import com.cts.hemant.tailorshop.entity.Appointment;

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
public class AppointmentDto {

	private long appointmentId;

	private LocalDate appointmentDate;

	private String status;

	private long customerId;

	private long shopId;

	private long categoryId;

	public Appointment mapToAppointment() {

		Appointment appointment = new Appointment();
		appointment.setAppointmentId(appointmentId);
		appointment.setAppointmentDate(appointmentDate);
		appointment.setStatus(status);

		return appointment;

	}
}
