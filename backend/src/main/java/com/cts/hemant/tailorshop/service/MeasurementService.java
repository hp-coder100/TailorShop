package com.cts.hemant.tailorshop.service;

import java.util.List;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.MeasurementDto;

public interface MeasurementService {

	public MeasurementDto createMeasurement(MeasurementDto measurementDto) throws ResourceNotFoundException;

	public void deleteMeasurement(long measurementId) throws ResourceNotFoundException;

	public MeasurementDto updateMeasurement(MeasurementDto measurementDto) throws ResourceNotFoundException;

	public MeasurementDto findByMeasurementId(long measurementId) throws ResourceNotFoundException;

	public List<MeasurementDto> findAllMeasurement();

	public List<MeasurementDto> findAllByCustomerId(long customerId) throws ResourceNotFoundException;

	public List<MeasurementDto> findAllByCustomerIdAndAppointmentId(long customerId,long appointmentId) throws ResourceNotFoundException;

	public List<MeasurementDto> findAllByCustomerIdAndShopId(long customerId, long shopId) throws ResourceNotFoundException;

	public List<MeasurementDto> findAllByShopIdAndAppointmentId(long shopId,long appointmentId) throws ResourceNotFoundException;

	public List<MeasurementDto> findAllByShopId(long shopId) throws ResourceNotFoundException;

}
