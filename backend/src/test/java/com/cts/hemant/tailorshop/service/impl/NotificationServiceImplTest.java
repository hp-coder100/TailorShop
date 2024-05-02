package com.cts.hemant.tailorshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.cts.hemant.tailorshop.entity.Notification;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.NotificationDto;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.NotificationRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.util.StaticStringValues;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private TailorRepository tailorRepository;

	@Mock
	private NotificationRepository notificationRepository;

	@InjectMocks
	private NotificationServiceImpl notificationService;

	private final Tailor TAILOR_1 = new Tailor(1L, "Kashif Umar", "Hello How are You doing", "Kashif@gmail.com",
			"1232323", null);

	private final Customer CUST_1 = new Customer(1L, "Hemant", "Prajapati", "Guna, MadhyaPradesh", 123456789,
			"Hemant@gmail.com", "123456");

	private final Notification NOT_1 = new Notification(1L, "Hello, How are You?", CUST_1, TAILOR_1);

	private final NotificationDto NOT_DTO_1 = new NotificationDto(1L, "Hello, How are You?", 1L, 1L);

	@Test
	void testCreateNotification()throws ResourceNotFoundException  {

		when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));

		when(notificationRepository.save(NOT_1)).thenReturn(NOT_1);

		NotificationDto notificationDto = notificationService.createNotification(NOT_DTO_1);

		verify(notificationRepository, times(1)).save(NOT_1);

		assertNotNull(notificationDto);

		assertThat(notificationDto.getNotificationId()).isEqualTo(NOT_1.getNotificationId());

	}

	@Test
	void testCreateNotification_whenNotificationIdInvalid()throws ResourceNotFoundException  {

		when(notificationRepository.findById(1L)).thenReturn(Optional.of(NOT_1));

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			notificationService.createNotification(NOT_DTO_1);
		});

		verify(notificationRepository, never()).save(NOT_1);

		assertThat(exception.getMessage().equals(StaticStringValues.NOTIFICATION_ALREADY_EXIST + 1));

	}

	@Test
	void testCreateNotification_whenCustomerIdInvalid() {

		when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			notificationService.createNotification(NOT_DTO_1);
		});

		verify(notificationRepository, never()).save(NOT_1);

		assertThat(exception.getMessage().equals(StaticStringValues.NOTIFICATION_DOES_NOT_EXIT_STRING + 1));

	}

	@Test
	void testCreateNotification_whenShopIdInvalid() {

		when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));

		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			notificationService.createNotification(NOT_DTO_1);
		});

		verify(notificationRepository, never()).save(NOT_1);

		assertThat((StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + 1).equals(exception.getMessage()));

	}

	@Test
	void testUpdateNotification() throws ResourceNotFoundException {

		when(notificationRepository.findById(1L)).thenReturn(Optional.of(NOT_1));

		when(notificationRepository.save(NOT_1)).thenReturn(NOT_1);

		NotificationDto notificationDto = notificationService.updateNotification(NOT_DTO_1);

		verify(notificationRepository, times(1)).findById(1L);
		verify(notificationRepository, times(1)).save(NOT_1);

		assertNotNull(notificationDto);

		assertThat(notificationDto.getNotificationId()).isEqualTo(NOT_1.getNotificationId());

	}

	@Test
	void testUpdateNotification_whenNotificaitonIdInvalid() {

		when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			notificationService.updateNotification(NOT_DTO_1);
		});

		verify(notificationRepository, never()).save(NOT_1);

		assertThat((StaticStringValues.NOTIFICATION_DOES_NOT_EXIT_STRING + 1).equals(exception.getMessage()));

	}

	@Test
	void testFindByNotificationId() throws ResourceNotFoundException {
		when(notificationRepository.findById(1L)).thenReturn(Optional.of(NOT_1));

		NotificationDto notificationDto = notificationService.findByNotificationId(1L);

		verify(notificationRepository, times(1)).findById(1L);

		assertNotNull(notificationDto);

		assertThat(notificationDto.getNotificationId()).isEqualTo(1L);

	}

	@Test
	void testFindByNotificationId_whenNotificaitonIdInvalid() {

		when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			notificationService.findByNotificationId(1L);
		});

		assertThat((StaticStringValues.NOTIFICATION_DOES_NOT_EXIT_STRING + 1).equals(exception.getMessage()));

	}

	@Test
	void testDeleteNotification()throws ResourceNotFoundException  {
		when(notificationRepository.findById(1L)).thenReturn(Optional.of(NOT_1));
		doNothing().when(notificationRepository).delete(NOT_1);

		notificationService.deleteNotification(1L);

		verify(notificationRepository, times(1)).findById(1L);
		verify(notificationRepository, times(1)).delete(NOT_1);
	}

	@Test
	void testDeleteNotification_whenNotificaitonIdInvalid() {

		when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			notificationService.deleteNotification(1L);
		});

		verify(notificationRepository, never()).delete(NOT_1);

		assertThat((StaticStringValues.NOTIFICATION_DOES_NOT_EXIT_STRING + 1).equals(exception.getMessage()));

	}

	@Test
	void testFindAllNotifications() {
		when(notificationRepository.findAll()).thenReturn(Arrays.asList(NOT_1));

		List<NotificationDto> list = notificationService.findAllNotifications();

		assertNotNull(list);
		assertThat(list.size()).isEqualTo(1);
	}

	@Test
	void testFindAllByCustomerId() throws ResourceNotFoundException {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(CUST_1));
		when(notificationRepository.findAllByCustomer_CustomerId(1L)).thenReturn(Arrays.asList(NOT_1));

		List<NotificationDto> list = notificationService.findAllByCustomerId(1L);

		verify(customerRepository, times(1)).findById(1L);

		assertNotNull(list);
		assertThat(list.size()).isEqualTo(1);
	}

	@Test
	void testFindAllByCustomerId_whenCustomerIdInvalid() {

		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			notificationService.findAllByCustomerId(1L);
		});

		assertThat((StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + 1).equals(exception.getMessage()));

	}

	@Test
	void testFindAllByShopId()throws ResourceNotFoundException  {
		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));
		when(notificationRepository.findAllByTailor_ShopId(1L)).thenReturn(Arrays.asList(NOT_1));

		List<NotificationDto> list = notificationService.findAllByShopId(1L);

		verify(tailorRepository, times(1)).findById(1L);

		assertNotNull(list);
		assertThat(list.size()).isEqualTo(1);
	}

	@Test
	void testFindAllByShopId_whenShopIdInvalid() {

		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			notificationService.findAllByShopId(1L);
		});

		assertThat((StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + 1).equals(exception.getMessage()));

	}
}
