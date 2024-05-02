package com.cts.hemant.tailorshop.service;

import java.time.LocalDate;
import java.util.List;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.AppointmentDto;

public interface AppointmentService {

	public AppointmentDto createAppointment(AppointmentDto appointment) throws ResourceNotFoundException;

	public AppointmentDto findAppointmentByAppointmentId(long appointmentId) throws ResourceNotFoundException;

	public void deleteAppointment(long appointmentId) throws ResourceNotFoundException;

	public AppointmentDto updateAppointment(AppointmentDto appointment) throws ResourceNotFoundException;

	public List<AppointmentDto> findAllAppointments();

	public List<AppointmentDto> findAllByCustomerId(long customerId) throws ResourceNotFoundException;

	public List<AppointmentDto> findAllByShopId(long shopId) throws ResourceNotFoundException;

	public List<AppointmentDto> findAllByCategoryId(long categoryId) throws ResourceNotFoundException;

	public List<AppointmentDto> findAllByDate(LocalDate date) throws ResourceNotFoundException;
	
	public List<AppointmentDto> findAllByCustomerIddAndShopId(long customerId, long shopId) throws ResourceNotFoundException;

	public List<AppointmentDto> findAllCustomerIdAndAppointmentDate(long customerId, LocalDate date) throws ResourceNotFoundException;

	public List<AppointmentDto> findAllByCustomerIdAndStatus(long customerId, String status) throws ResourceNotFoundException;

	public List<AppointmentDto> findAllByCustomerIdAndCategoryId(long customerId, long categoryId) throws ResourceNotFoundException;

	
	public List<AppointmentDto> findAllByShopIdAndAppointmentDate(long shopId, LocalDate date) throws ResourceNotFoundException;

	public List<AppointmentDto> findAllByShopIdAndStatus(long shopId, String status) throws ResourceNotFoundException;

	public List<AppointmentDto> findAllByShopIdAndCategoryId(long shopId, long categoryId) throws ResourceNotFoundException;
}
