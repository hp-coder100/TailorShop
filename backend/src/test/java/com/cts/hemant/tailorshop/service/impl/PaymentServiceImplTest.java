package com.cts.hemant.tailorshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.hemant.tailorshop.entity.Appointment;
import com.cts.hemant.tailorshop.entity.Category;
import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Payment;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.PaymentDto;
import com.cts.hemant.tailorshop.repository.AppointmentRepository;
import com.cts.hemant.tailorshop.repository.PaymentRepository;
import com.cts.hemant.tailorshop.util.StaticStringValues;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

	@Mock
	private AppointmentRepository appointmentRepository;

	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private PaymentServiceImpl paymentService;

	private final Customer CUST_1 = new Customer(1L, "Hemant", "Prajapati", "Guna, MadhyaPradesh", 123456789,
			"Hemant@gmail.com", "123456");

	private final Tailor TAILOR_1 = new Tailor(1L, "Kashif Umar", "Hello How are You doing", "Kashif@gmail.com",
			"1232323", null);

	private final Category CAT_1 = new Category(1L, "Kurta Pazam", "Slim Stylish Outfit", TAILOR_1, null);

	private final Appointment APPOINTMENT_1 = new Appointment(1L, LocalDate.parse("2023-05-11"), "Pending", CUST_1,
			TAILOR_1, CAT_1);

	private final Payment PAY_1 = new Payment(1L, 1000.0, LocalDate.parse("2023-02-23"), "Pending", APPOINTMENT_1),
			PAY_2 = new Payment(2L, 2000.0, LocalDate.parse("2023-02-23"), "Success", APPOINTMENT_1);

	private final PaymentDto PAY_DTO_1 = new PaymentDto(1L, 1000.0, LocalDate.parse("2023-02-23"), "Pending", 1L);

	@Test
	void testCreatePayment() throws ResourceNotFoundException {
		when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));

		when(paymentRepository.save(PAY_1)).thenReturn(PAY_1);

		PaymentDto paymentDto = paymentService.createPayment(PAY_DTO_1);

		verify(paymentRepository, times(1)).save(PAY_1);
		assertNotNull(paymentDto);
		assertThat(paymentDto.getAppointmentId()).isEqualTo(PAY_DTO_1.getAppointmentId());

	}

	@Test
	void testCreatePayment_whenPaymentIdInvalid() {

		when(paymentRepository.findById(1L)).thenReturn(Optional.of(PAY_1));

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			paymentService.createPayment(PAY_DTO_1);
		});
		verify(paymentRepository, never()).save(PAY_1);
		assertThat(exception.getMessage().equals(StaticStringValues.PAYMENT_ALREADY_EXIST + "1"));

	}

	@Test
	void testCreatePayment_whenAppointmentIdInvalid() {
		when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
		when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			paymentService.createPayment(PAY_DTO_1);
		});
		verify(paymentRepository, never()).save(PAY_1);
		assertThat(exception.getMessage().equals(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testDeletePayment() throws ResourceNotFoundException {
		when(paymentRepository.findById(1L)).thenReturn(Optional.of(PAY_1));
		doNothing().when(paymentRepository).delete(PAY_1);

		paymentService.deletePayment(1L);

		verify(paymentRepository, times(1)).findById(1L);
		verify(paymentRepository, times(1)).delete(PAY_1);

	}

	@Test
	void testDeletePayment_whenPaymentIdInvalid() {
		when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			paymentService.deletePayment(1L);
		});
		verify(paymentRepository, never()).delete(PAY_1);
		assertThat(exception.getMessage().equals(StaticStringValues.PAYMENT_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testUpdatePayment()throws ResourceNotFoundException  {
		when(paymentRepository.findById(1L)).thenReturn(Optional.of(PAY_1));
		when(paymentRepository.save(PAY_1)).thenReturn(PAY_1);

		PaymentDto paymentDto = paymentService.updatePayment(PAY_DTO_1);
		verify(paymentRepository, times(1)).save(PAY_1);
		assertNotNull(paymentDto);
		assertThat(paymentDto.getAppointmentId()).isEqualTo(PAY_DTO_1.getAppointmentId());

	}

	@Test
	void testUpdatePayment_whenPaymentIdInvalid() {
		when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			paymentService.updatePayment(PAY_DTO_1);
		});
		verify(paymentRepository, never()).save(PAY_1);
		assertThat(exception.getMessage().equals(StaticStringValues.PAYMENT_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testFindAllByAppointmentId() throws ResourceNotFoundException {
		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));
		when(paymentRepository.findAllByAppointment_AppointmentId(1L)).thenReturn(Arrays.asList(PAY_1, PAY_2));

		List<PaymentDto> list = paymentService.findAllByAppointmentId(1L);

		verify(paymentRepository, times(1)).findAllByAppointment_AppointmentId(1L);
		assertNotNull(list);
		assertThat(list.size()).isEqualTo(2);

	}

	@Test
	void testFindAllByAppointmentId_whenAppointmentIdInvalid() {
		when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			paymentService.findAllByAppointmentId(1L);
		});
		verify(paymentRepository, never()).findAllByAppointment_AppointmentId(1L);
		assertThat(exception.getMessage().equals(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testFindByPaymentId() throws ResourceNotFoundException {
		when(paymentRepository.findById(1L)).thenReturn(Optional.of(PAY_1));

		PaymentDto paymentDto = paymentService.findByPaymentId(1L);

		verify(paymentRepository, times(1)).findById(1L);
		assertNotNull(paymentDto);
		assertThat(paymentDto.getAppointmentId()).isEqualTo(PAY_DTO_1.getAppointmentId());
	}

	@Test
	void testFindByPaymentId_whenPaymentIdInvalid() {
		when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			paymentService.findByPaymentId(1L);
		});
		verify(paymentRepository, times(1)).findById(1L);
		assertThat(exception.getMessage().equals(StaticStringValues.PAYMENT_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testFindAllPayments() {
		when(paymentRepository.findAll()).thenReturn(Arrays.asList(PAY_1, PAY_2));

		List<PaymentDto> list = paymentService.findAllPayments();

		verify(paymentRepository, times(1)).findAll();
		assertNotNull(list);
		assertThat(list.size()).isEqualTo(2);
	}

	@Test
	void testFindAllByStatus() throws ResourceNotFoundException {
		when(paymentRepository.findAllByStatus("Pending")).thenReturn(Arrays.asList(PAY_1));

		List<PaymentDto> list = paymentService.findAllByStatus("Pending");

		verify(paymentRepository, times(1)).findAllByStatus("Pending");
		assertNotNull(list);
		assertThat(list.size()).isEqualTo(1);
	}

	@Test
	void testFindAllByDate() throws ResourceNotFoundException {

		when(paymentRepository.findAllByPaymentDate(LocalDate.parse("2023-02-23")))
				.thenReturn(Arrays.asList(PAY_1, PAY_2));

		List<PaymentDto> list = paymentService.findAllByDate(LocalDate.parse("2023-02-23"));

		verify(paymentRepository, times(1)).findAllByPaymentDate(LocalDate.parse("2023-02-23"));
		assertNotNull(list);
		assertThat(list.size()).isEqualTo(2);
	}

}
