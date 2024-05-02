package com.cts.hemant.tailorshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.AppointmentDto;
import com.cts.hemant.tailorshop.repository.AppointmentRepository;
import com.cts.hemant.tailorshop.repository.CategoryRepository;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.util.StaticStringValues;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

	@Mock
	private AppointmentRepository appointmentRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private TailorRepository tailorRepository;

	@Mock
	private CategoryRepository categoryRepository;
	@InjectMocks
	private AppointmentServiceImpl appointmentService;

	private final Customer CUST_1 = new Customer(1L, "Hemant", "Prajapati", "Guna, MadhyaPradesh", 123456789,
			"Hemant@gmail.com", "123456");

	private final Tailor TAILOR_1 = new Tailor(1L, "Kashif Umar", "Hello How are You doing", "Kashif@gmail.com",
			"1232323", null);

	private final Category CAT_1 = new Category(1L, "Kurta Pazam", "Slim Stylish Outfit", TAILOR_1, null);

	private final Appointment APPOINTMENT_1 = new Appointment(1L, LocalDate.parse("2023-05-11"), "Pending", CUST_1,
			TAILOR_1, CAT_1),
			APPOINTMENT_2 = new Appointment(2L, LocalDate.parse("2023-05-18"), "Pending", CUST_1, TAILOR_1, CAT_1),
			APPOINTMENT_3 = new Appointment(3L, LocalDate.parse("2023-06-24"), "Success", CUST_1, TAILOR_1, CAT_1);

	private final AppointmentDto APPOINTMENT_DTO_1 = new AppointmentDto(1L, LocalDate.parse("2023-05-11"), "Pending", 1,
			1, 1);
	
	
	/*
	 * 
	 * Testing createAppointment method
	 * 
	 */

	@Test
	public void test_createAppointment_forValidAppointment() throws ResourceNotFoundException {

		when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));

		when(categoryRepository.findById(1L)).thenReturn(Optional.of(CAT_1));

		when(appointmentRepository.save(APPOINTMENT_1)).thenReturn(APPOINTMENT_1);
		
		AppointmentDto appointmentDto = appointmentService.createAppointment(APPOINTMENT_DTO_1);
		verify(appointmentRepository, times(1)).save(APPOINTMENT_1);

		assertNotNull(appointmentDto);
		assertEquals(appointmentDto.getAppointmentId(), APPOINTMENT_1.getAppointmentId());
	}


	@Test
	public void test_createAppointment_forInvalidAppointmentId() {

		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.createAppointment(APPOINTMENT_DTO_1);
		});

		verify(appointmentRepository, never()).save(APPOINTMENT_1);

		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(StaticStringValues.APPOINTMENT_ALREADY_EXIST+1));
	}

	@Test
	public void test_createAppointment_forInvalidCustomerId() {
		

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.createAppointment(APPOINTMENT_DTO_1);
		});

		String actualMessage = exception.getMessage();
		verify(appointmentRepository, never()).save(APPOINTMENT_1);

		assertTrue(actualMessage.contains(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING+1));
	}

	@Test
	public void test_createAppointment_forInvalidShopId() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.createAppointment(APPOINTMENT_DTO_1);
		});

		String actualMessage = exception.getMessage();
		verify(appointmentRepository, never()).save(APPOINTMENT_1);

		assertTrue(actualMessage.contains(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING+1));
	}

	@Test
	public void test_createAppointment_forInvalidCategoryId() {

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));

		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.createAppointment(APPOINTMENT_DTO_1);
		});

		String actualMessage = exception.getMessage();
		verify(appointmentRepository, never()).save(APPOINTMENT_1);

		assertTrue(actualMessage.contains(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING+1));
	}

	/*
	 * 
	 * Testing findAppointmentByAppointmentId method
	 * 
	 */

	@Test
	public void test_findAppointmentByAppointmentId_whenAppointmentFound()throws ResourceNotFoundException  {
		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));

		AppointmentDto appointmentDto = appointmentService.findAppointmentByAppointmentId(1L);

		assertNotNull(appointmentDto);

		assertEquals(appointmentDto.getAppointmentDate(), APPOINTMENT_1.getAppointmentDate());
		assertEquals(appointmentDto.getAppointmentId(), APPOINTMENT_1.getAppointmentId());

	}

	@Test
	public void test_findAppointmentByAppointmentId_whenAppointmentNotFound() {
		when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.findAppointmentByAppointmentId(1L);
		});

		String actualMessage = exception.getMessage();
		verify(appointmentRepository, never()).save(APPOINTMENT_1);

		assertTrue(actualMessage.contains(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING+1));

	}

	/*
	 * 
	 * Testing deleteAppointment method
	 * 
	 */

	@Test
	public void test_deleteAppointment_forValidAppointment() throws ResourceNotFoundException {

		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));

		doNothing().when(appointmentRepository).delete(APPOINTMENT_1);

		appointmentService.deleteAppointment(1L);

		verify(appointmentRepository, times(1)).delete(APPOINTMENT_1);

	}

	@Test
	public void test_deleteAppointment_forInvalidAppointment() {


		when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.deleteAppointment(1L);
		});

		String actualMessage = exception.getMessage();

		verify(appointmentRepository, never()).delete(APPOINTMENT_1);

		assertTrue(actualMessage.contains(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING+1));

	}

	/*
	 * 
	 * Testing updateAppointment method
	 * 
	 */

	@Test
	public void test_updateAppointment_forValidAppointment() throws ResourceNotFoundException {
		when(appointmentRepository.findById(1L)).thenReturn(Optional.of(APPOINTMENT_1));

		when(appointmentRepository.save(APPOINTMENT_1)).thenReturn(APPOINTMENT_1);

		AppointmentDto updateAppointmentDto = appointmentService.updateAppointment(APPOINTMENT_DTO_1);

		verify(appointmentRepository, times(1)).save(APPOINTMENT_1);

		assertThat(updateAppointmentDto.getAppointmentDate()).isEqualTo(APPOINTMENT_1.getAppointmentDate());

		assertThat(updateAppointmentDto.getStatus()).isEqualTo(APPOINTMENT_1.getStatus());
	}

	@Test
	public void test_updateAppointment_forInvalidAppointment() {

		when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.updateAppointment(APPOINTMENT_DTO_1);
		});

		String actualMessage = exception.getMessage();

		verify(appointmentRepository, never()).save(APPOINTMENT_1);

		assertTrue(actualMessage.contains(StaticStringValues.APPOINTMENT_DOES_NOT_EXIT_STRING+1));
	}

	/*
	 * 
	 * Testing findAllAppointments method
	 * 
	 */
	@Test
	public void test_findAllAppointments() {

		when(appointmentRepository.findAll()).thenReturn(Arrays.asList(APPOINTMENT_1, APPOINTMENT_2, APPOINTMENT_3));

		List<AppointmentDto> list = appointmentService.findAllAppointments();

		assertNotNull(list);

		assertThat(list.size()).isEqualTo(3);

	}

	/*
	 * 
	 * Testing findAllByCustomerId method
	 * 
	 */

	@Test
	public void test_findAllByCustomerId_forInValidCustomerId() {

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.findAllByCustomerId(1L);
		});

		String actualMessage = exception.getMessage();
		verify(appointmentRepository, never()).findAllByCustomer_CustomerId(1L);

		assertTrue(actualMessage.contains(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING+1));
	}

	@Test
	public void test_findAllByCustomerId_forValidCustomerId() throws ResourceNotFoundException {

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		when(appointmentRepository.findAllByCustomer_CustomerId(1L)).thenReturn(Arrays.asList(APPOINTMENT_1));

		List<AppointmentDto> list = appointmentService.findAllByCustomerId(1L);

		verify(appointmentRepository, times(1)).findAllByCustomer_CustomerId(1L);

		assertThat(list.size()).isEqualTo(1);

		assertThat(list.get(0).getAppointmentId()).isEqualTo(1L);

	}
	/*
	 * 
	 * Testing findAllByShopId method
	 * 
	 */

	@Test
	public void test_findAllByShopId_forInValidShopId() {

		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.findAllByShopId(1L);
		});

		String actualMessage = exception.getMessage();
		verify(appointmentRepository, never()).findAllByTailor_ShopId(1L);

		assertTrue(actualMessage.contains(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING+1));
	}

	@Test
	public void test_findAllByShopId_forValidShopId()throws ResourceNotFoundException  {

		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));

		when(appointmentRepository.findAllByTailor_ShopId(1L)).thenReturn(Arrays.asList(APPOINTMENT_3));

		List<AppointmentDto> list = appointmentService.findAllByShopId(1L);

		verify(appointmentRepository, times(1)).findAllByTailor_ShopId(1L);

		assertThat(list.size()).isEqualTo(1);

		assertThat(list.get(0).getAppointmentId()).isEqualTo(3L);

	}

	/*
	 * 
	 * Testing findAllByCategoryId method
	 * 
	 */

	@Test
	public void test_findAllByCategoryId_forInValidCategoryId()throws ResourceNotFoundException  {

		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.findAllByCategoryId(1L);
		});

		String actualMessage = exception.getMessage();
		verify(appointmentRepository, never()).findAllByCategory_CategoryId(1L);

		assertTrue(actualMessage.contains(StaticStringValues.CATEGORY_DOES_NOT_EXIT_STRING+1));
	}

	
	@Test
	public void test_findAllByCategoryId_forValidCategoryId() throws ResourceNotFoundException {

		when(categoryRepository.findById(1L)).thenReturn(Optional.of(CAT_1));

		when(appointmentRepository.findAllByCategory_CategoryId(1L)).thenReturn(Arrays.asList(APPOINTMENT_1));

		List<AppointmentDto> list = appointmentService.findAllByCategoryId(1L);

		verify(appointmentRepository, times(1)).findAllByCategory_CategoryId(1L);

		assertThat(list.size()).isEqualTo(1);

		assertThat(list.get(0).getAppointmentId()).isEqualTo(1L);

	}

	/*
	 * 
	 * Testing findAllByDate method
	 * 
	 */

	@Test
	public void test_findAllByDate() throws ResourceNotFoundException {

		when(appointmentRepository.findAllByAppointmentDate(LocalDate.parse("2023-05-11")))
				.thenReturn(Arrays.asList(APPOINTMENT_1));

		List<AppointmentDto> list = appointmentService.findAllByDate(LocalDate.parse("2023-05-11"));

		verify(appointmentRepository, times(1)).findAllByAppointmentDate(LocalDate.parse("2023-05-11"));

		assertNotNull(list);

		assertThat(list.size()).isEqualTo(1);

	}

}
