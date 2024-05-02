package com.cts.hemant.tailorshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.CustomerDto;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.util.StaticStringValues;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepository;
	
	@Mock 
	private TailorRepository tailorRepository;

	@InjectMocks
	private CustomerServiceImpl customerService;

	private final Customer CUST_1 = new Customer(1L, "Hemant", "Prajapati", "Guna, MadhyaPradesh", 123456789,
			"Hemant@gmail.com", "123456"),
			CUST_2 = new Customer(2L, "Hemant", "Prajapati", "Guna, MadhyaPradesh", 123456789, "Hemant@gmail.com",
					"123456"),
			CUST_3 = new Customer(3L, "Hemant", "Prajapati", "Guna, MadhyaPradesh", 123456789, "Hemant@gmail.com",
					"123456");

	private final CustomerDto CUSTOMER_DTO_1 = new CustomerDto(1L, "Hemant", "Prajapati", "Guna, MadhyaPradesh",
			123456789, "Hemant@gmail.com", "123456");

	@Test
	void testCreateCustomer() throws ResourceNotFoundException {
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());
		
		when(customerRepository.findByEmail(CUSTOMER_DTO_1.getEmail())).thenReturn(Optional.empty());
		
		when(tailorRepository.findByEmail(CUSTOMER_DTO_1.getEmail())).thenReturn(Optional.empty());
		
		when(customerRepository.save(CUST_1)).thenReturn(CUST_1);

		CustomerDto customerDto = customerService.createCustomer(CUSTOMER_DTO_1);

		verify(customerRepository, times(1)).findById(1L);

		verify(customerRepository, times(1)).save(CUST_1);

		assertThat(customerDto.getCustomerId()).isEqualTo(CUST_1.getCustomerId());
	}

	@Test
	public void testCreateCustomer_whenCutomerIdInvalid() {

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));
assertThrows(ResourceNotFoundException.class, () -> {
			customerService.createCustomer(CUSTOMER_DTO_1);
		});

		

		verify(customerRepository, times(1)).findById(1L);

		verify(customerRepository, never()).save(CUST_1);
		
	}
	
	@Test
	public void testCreateCustomer_whenTailorEmailIdInvalid() {
		when(customerRepository.findByEmail(CUSTOMER_DTO_1.getEmail())).thenReturn(Optional.of(CUST_1));
		
		assertThrows(ResourceNotFoundException.class, () -> {
			customerService.createCustomer(CUSTOMER_DTO_1);
		});

		

		verify(customerRepository, times(1)).findById(1L);

		verify(customerRepository, never()).save(CUST_1);
		

	}
	
	@Test
	public void testCreateCustomer_whenCutomerEmailIdInvalid() {

		when(tailorRepository.findByEmail(CUSTOMER_DTO_1.getEmail())).thenReturn(Optional.of(new Tailor()));
		assertThrows(ResourceNotFoundException.class, () -> {
			customerService.createCustomer(CUSTOMER_DTO_1);
		});

		

		verify(customerRepository, times(1)).findById(1L);

		verify(customerRepository, never()).save(CUST_1);

	}

	@Test
	void testUpdateCustomer() throws ResourceNotFoundException {

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));
		when(customerRepository.save(CUST_1)).thenReturn(CUST_1);

		CustomerDto customerDto = customerService.updateCustomer(CUSTOMER_DTO_1);

		verify(customerRepository, times(1)).findById(1L);

		verify(customerRepository, times(1)).save(CUST_1);

		assertThat(customerDto.getCustomerId()).isEqualTo(CUST_1.getCustomerId());

	}

	@Test
	void testUpdateCustomer_whenCustomerIdIsInvalid() {

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			customerService.updateCustomer(CUSTOMER_DTO_1);
		});

		String actualMessage = exception.getMessage();

		verify(customerRepository, times(1)).findById(1L);

		verify(customerRepository, never()).save(CUST_1);

		assertTrue(actualMessage.contains(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING+1));
	}

	@Test
	void testFindByCustomerId_whenInvalidCustomerId() {

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			customerService.findByCustomerId(1L);
		});
		
		String actualMessage = exception.getMessage();

		verify(customerRepository, times(1)).findById(1L);

		assertTrue(actualMessage.contains(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING+1));
	}

	@Test
	void testFindByCustomerId() throws ResourceNotFoundException {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		CustomerDto customerDto = customerService.findByCustomerId(1L);

		verify(customerRepository, times(1)).findById(1L);

		assertThat(customerDto.getCustomerId()).isEqualTo(CUST_1.getCustomerId());

	}

	@Test
	void testDeleteCustomer_whenCustomerIdIsInvalid() {

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			customerService.deleteCustomer(1L);
		});

		String actualMessage = exception.getMessage();

		verify(customerRepository, times(1)).findById(1L);

		verify(customerRepository, never()).delete(CUST_1);

		assertTrue(actualMessage.contains(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING+1));
	}

	@Test
	void testDeleteCustomer()throws ResourceNotFoundException  {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		doNothing().when(customerRepository).delete(CUST_1);

		customerService.deleteCustomer(1L);

		verify(customerRepository, times(1)).findById(1L);

		verify(customerRepository, times(1)).delete(CUST_1);

	}

	@Test
	void testFindByEmailId() throws ResourceNotFoundException {
		when(customerRepository.findByEmail("Hemant@gmail.com")).thenReturn(Optional.of(CUST_1));
		
		CustomerDto customerDto = customerService.findByEmailId("Hemant@gmail.com");
		
		verify(customerRepository, times(1)).findByEmail("Hemant@gmail.com");
		
		assertThat(customerDto.getEmail()).isEqualTo("Hemant@gmail.com");
		
	}
	
	@Test
	void testFindByEmailId_whenInvalid() {

		when(customerRepository.findByEmail("Hemant@gmail.com")).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			customerService.findByEmailId("Hemant@gmail.com");
		});

		String actualMessage = exception.getMessage();

		verify(customerRepository, times(1)).findByEmail("Hemant@gmail.com");

		assertTrue(actualMessage.contains(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING+"Hemant@gmail.com"));
	}

	@Test
	void testFindAllCustomers() {
		when(customerRepository.findAll()).thenReturn(Arrays.asList(CUST_1, CUST_2,CUST_3));
		
		
		List<CustomerDto> list = customerService.findAllCustomers();
		
		verify(customerRepository, times(1)).findAll()
;		assertThat(list.size()).isEqualTo(3);
		
	}

}
