package com.cts.hemant.tailorshop.service;

import java.time.LocalDate;
import java.util.List;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.PaymentDto;

public interface PaymentService {

	public PaymentDto createPayment(PaymentDto paymentDto) throws ResourceNotFoundException;

	public void deletePayment(long paymentId) throws ResourceNotFoundException;

	public PaymentDto updatePayment(PaymentDto paymentDto) throws ResourceNotFoundException;

	public List<PaymentDto> findAllByAppointmentId(long appointmentId) throws ResourceNotFoundException;

	public PaymentDto findByPaymentId(long paymentId) throws ResourceNotFoundException;

	public List<PaymentDto> findAllPayments();

	public List<PaymentDto> findAllByStatus(String status) throws ResourceNotFoundException;

	public List<PaymentDto> findAllByDate(LocalDate date) throws ResourceNotFoundException;

	public List<PaymentDto> findAllByCustomerId(long customerId) throws ResourceNotFoundException;

	public List<PaymentDto> findAllByCustomerIdAndStatus(long customerId, String status) throws ResourceNotFoundException;

	public List<PaymentDto> findAllByCustomerIdAndPaymentDate(long customerId, LocalDate date) throws ResourceNotFoundException;

	public List<PaymentDto> findAllByShopId(long shopId) throws ResourceNotFoundException;

	public List<PaymentDto> findAllByShopIdAndStatus(long shopId, String status) throws ResourceNotFoundException;

	public List<PaymentDto> findAllByShopIdAndPaymentDate(long shopId, LocalDate date) throws ResourceNotFoundException;

}
