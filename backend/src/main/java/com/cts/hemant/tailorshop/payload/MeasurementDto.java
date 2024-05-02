package com.cts.hemant.tailorshop.payload;

import com.cts.hemant.tailorshop.entity.Measurement;

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
public class MeasurementDto {

	private long measurementId;

	private String details;

	private long appointmentId;
	private long customerId;
	private long shopId;
	
	public Measurement mapToMeasurement() {
		return new Measurement(measurementId, details, null, null, null);
	}
}
