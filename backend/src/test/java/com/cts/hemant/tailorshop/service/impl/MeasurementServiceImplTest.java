package com.cts.hemant.tailorshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.cts.hemant.tailorshop.entity.Measurement;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.MeasurementDto;
import com.cts.hemant.tailorshop.repository.AppointmentRepository;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.MeasurementRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.util.StaticStringValues;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceImplTest {

	@Mock
	private MeasurementRepository measurementRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private TailorRepository tailorRepository;

	@Mock
	private AppointmentRepository appointmentRepository;

	@InjectMocks
	private MeasurementServiceImpl measurementService;

	private final Tailor TAILOR_1 = new Tailor(1L, "Kashif Umar", "Hello How are You doing", "Kashif@gmail.com",
			"1232323", null);

	private final Category CAT_1 = new Category(1L, "Kurta Pazam", "Slim Stylish Outfit", TAILOR_1, null);

	private Customer CUST_1 = new Customer(1L, "Hemant", "Prajapati", "Guna, MadhyaPradesh", 123456789,
			"Hemant@gmail.com", "123456");

	private final Appointment APPOINTMENT_1 = new Appointment(1L, LocalDate.parse("2023-05-11"), "Pending", CUST_1,
			TAILOR_1, CAT_1);

	private final Measurement MEAS_1 = new Measurement(1L, "sljflasljdfl", APPOINTMENT_1, CUST_1, TAILOR_1);

	private final MeasurementDto MEAS_DTO_1 = new MeasurementDto(1L, "sljflasljdfl", 1L, 1L, 1L);

	@Test
	void testCreateMeasurement() throws ResourceNotFoundException {

		when(measurementRepository.findById(1L)).thenReturn(Optional.empty());

		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));

		when(measurementRepository.save(MEAS_1)).thenReturn(MEAS_1);

		MeasurementDto measurementDto = measurementService.createMeasurement(MEAS_DTO_1);

		verify(measurementRepository, times(1)).save(MEAS_1);

		assertThat(measurementDto.getAppointmentId()).isEqualTo(MEAS_1.getMeasurementId());

	}

	@Test
	void testCreateMeasurement_MeasurementIdInvalid() {

		when(measurementRepository.findById(1L)).thenReturn(Optional.of(MEAS_1));

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			measurementService.createMeasurement(MEAS_DTO_1);
		});

		String actualMessage = exception.getMessage();

		verify(measurementRepository, times(1)).findById(1L);
		verify(measurementRepository, never()).save(MEAS_1);

		assertTrue(actualMessage.contains(StaticStringValues.MEASUREMENT_ALREADY_EXIST + 1));

	}

	@Test
	void testCreateMeasurement_AppointmentIdInvalid() {

		when(measurementRepository.findById(1L)).thenReturn(Optional.empty());

		when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			measurementService.createMeasurement(MEAS_DTO_1);
		});

		String actualMessage = exception.getMessage();

		verify(measurementRepository, never()).save(MEAS_1);

		assertTrue(actualMessage.contains(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + 1));

	}

	@Test
	void testCreateMeasurement_CustomerIdInvalid() {

		when(measurementRepository.findById(1L)).thenReturn(Optional.empty());

		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			measurementService.createMeasurement(MEAS_DTO_1);
		});

		String actualMessage = exception.getMessage();

		verify(measurementRepository, never()).save(MEAS_1);

		assertTrue(actualMessage.contains(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testCreateMeasurement_ShopIdInvalid() {

		when(measurementRepository.findById(1L)).thenReturn(Optional.empty());

		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			measurementService.createMeasurement(MEAS_DTO_1);
		});

		String actualMessage = exception.getMessage();

		verify(measurementRepository, never()).save(MEAS_1);

		assertTrue(actualMessage.contains(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testDeleteMeasurement_whenInvalidId() {

		when(measurementRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			measurementService.deleteMeasurement(1L);
		});

		String actualMessage = exception.getMessage();

		verify(measurementRepository, never()).delete(MEAS_1);

		assertTrue(actualMessage.contains(StaticStringValues.MEASUREMENT_DOES_NOT_EXIT_STRING + 1));

	}

	@Test
	void testDeleteMeasurement()throws ResourceNotFoundException  {

		when(measurementRepository.findById(1L)).thenReturn(Optional.of(MEAS_1));

		doNothing().when(measurementRepository).delete(MEAS_1);

		measurementService.deleteMeasurement(1L);

		verify(measurementRepository, times(1)).findById(1L);

		verify(measurementRepository, times(1)).delete(MEAS_1);

	}

	@Test
	void testUpdateMeasurement_whenMeasuementIdInvalid() {

		when(measurementRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			measurementService.updateMeasurement(MEAS_DTO_1);
		});

		String actualMessage = exception.getMessage();

		verify(measurementRepository, never()).save(MEAS_1);

		assertTrue(actualMessage.contains(StaticStringValues.MEASUREMENT_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testUpdateMeasurement()throws ResourceNotFoundException  {
		when(measurementRepository.findById(1L)).thenReturn(Optional.of(MEAS_1));
		when(measurementRepository.save(MEAS_1)).thenReturn(MEAS_1);

		MeasurementDto measurementDto = measurementService.updateMeasurement(MEAS_DTO_1);

		verify(measurementRepository, times(1)).findById(1L);
		verify(measurementRepository, times(1)).save(MEAS_1);

		assertThat(measurementDto.getAppointmentId()).isEqualTo(MEAS_1.getMeasurementId());

	}

//	@Test
//	void testFindByAppointmentId_whenAppointmentIdIsInvalid() {
//
//		when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());
//
//		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
//			measurementService.findByAppointmentId(1L);
//		});
//
//		String actualMessage = exception.getMessage();
//
//		verify(measurementRepository, never()).findByAppointment_AppointmentId(1L);
//
//		assertTrue(actualMessage.contains(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING + 1));
//	}

//	@Test
//	void testFindByAppointmentId() {
//
//		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));
//
//		when(measurementRepository.findByAppointment_AppointmentId(1L)).thenReturn(Arrays.asList(MEAS_1));
//
//		List<MeasurementDto> list = measurementService.findByAppointmentId(1L);
//
//		verify(measurementRepository, times(1)).findByAppointment_AppointmentId(1L);
//
//		assertNotNull(list);
//		assertThat(list.size()).isEqualTo(1);
//
//	}

	@Test
	void testFindByMeasurementId_whenMeasurementIdInvaild() {

		when(measurementRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			measurementService.findByMeasurementId(1L);
		});

		String actualMessage = exception.getMessage();

		verify(measurementRepository, times(1)).findById(1L);

		assertTrue(actualMessage.contains(StaticStringValues.MEASUREMENT_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testFindByMeasurementId() throws ResourceNotFoundException {

		when(measurementRepository.findById(1L)).thenReturn(Optional.of(MEAS_1));

		MeasurementDto measurementDto = measurementService.findByMeasurementId(1L);

		verify(measurementRepository, times(1)).findById(1L);

		assertThat(measurementDto.getAppointmentId()).isEqualTo(MEAS_1.getMeasurementId());

	}

	@Test
	void testFindAllMeasurement() {
		when(measurementRepository.findAll()).thenReturn(Arrays.asList(MEAS_1));

		List<MeasurementDto> list = measurementService.findAllMeasurement();

		assertNotNull(list);
		assertThat(list.size()).isEqualTo(1);

	}

	@Test
	void testFindAllByCustomerId_whenCustomerIdInvalid() {

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			measurementService.findAllByCustomerId(1L);
		});

		String actualMessage = exception.getMessage();

		verify(measurementRepository, never()).findByCustomer_CustomerId(1L);

		assertTrue(actualMessage.contains(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + 1));
	}

	@Test
	void testFindAllByCustomerId() throws ResourceNotFoundException {

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		when(measurementRepository.findByCustomer_CustomerId(1L)).thenReturn(Arrays.asList(MEAS_1));
		List<MeasurementDto> list = measurementService.findAllByCustomerId(1L);
		verify(measurementRepository, times(1)).findByCustomer_CustomerId(1L);

		assertNotNull(list);
		assertThat(list.size()).isEqualTo(1);

	}

}
