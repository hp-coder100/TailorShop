package com.cts.hemant.tailorshop.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Notification;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.NotificationDto;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.NotificationRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.service.NotificationService;
import com.cts.hemant.tailorshop.util.StaticStringValues;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TailorRepository tailorRepository;

	/**
	 * Creates a new notification based on the provided NotificationDto.
	 * 
	 * @param notificationDto Data transfer object containing notification details.
	 * @return The created NotificationDto with persisted data.
	 * @throws ResourceNotFoundException if the notification already exists or
	 *                                   related entities are not found.
	 */
	@Override
	public NotificationDto createNotification(NotificationDto notificationDto) throws ResourceNotFoundException {
		// Log the creation request
		log.info(StaticStringValues.REQUESTING_NOTIFICATION_CREATE);

		// Check for existing notification with the same ID
		if (notificationRepository.findById(notificationDto.getNotificationId()).isPresent()) {
			throw new ResourceNotFoundException(
					StaticStringValues.NOTIFICATION_ALREADY_EXIST + notificationDto.getNotificationId());
		}

		// Validate customer existence
		Optional<Customer> customerOptional = customerRepository.findById(notificationDto.getCustomerId());
		if (customerOptional.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + notificationDto.getCustomerId());
		}

		// Validate tailor existence
		Optional<Tailor> tailorOptional = tailorRepository.findById(notificationDto.getShopId());
		if (tailorOptional.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + notificationDto.getShopId());
		}

		// Map DTO to entity and set relationships
		Notification notification = notificationDto.mapToNotification();
		notification.setCustomer(customerOptional.get());
		notification.setTailor(tailorOptional.get());

		// Save the notification and map back to DTO
		NotificationDto savedNotificationDto = notificationRepository.save(notification).mapToNotificationDto();

		// Log the successful creation
		log.debug(StaticStringValues.NOTIFICATION_CREATED_SUCCESSFULLY + " " + savedNotificationDto);
		log.info(StaticStringValues.NOTIFICATION_CREATED_SUCCESSFULLY);
		return savedNotificationDto;
	}

	/**
	 * Updates an existing notification based on the provided NotificationDto.
	 * 
	 * @param notificationDto Data transfer object containing notification details.
	 * @return The updated NotificationDto with persisted data.
	 * @throws ResourceNotFoundException if the notification does not exist.
	 */
	@Override
	public NotificationDto updateNotification(NotificationDto notificationDto) throws ResourceNotFoundException {
		// Log the update request
		log.info(StaticStringValues.REQUEST_NOTIFICATION_UPDATE);

		// Check for existing notification with the same ID
		Optional<Notification> notificationOptional = notificationRepository
				.findById(notificationDto.getNotificationId());
		if (notificationOptional.isEmpty()) {
			throw new ResourceNotFoundException(
					StaticStringValues.NOTIFICATION_DOES_NOT_EXIT_STRING + notificationDto.getNotificationId());
		}

		// Update details if provided
		Notification notification = notificationOptional.get();
		if (notificationDto.getMessage() != null)
			notification.setMessage(notificationDto.getMessage());

		// Save the updated notification and map back to DTO
		NotificationDto updatedNotificationDto = notificationRepository.save(notification).mapToNotificationDto();

		// Log the successful update
		log.debug(StaticStringValues.NOTIFICATION_UPDATED_SUCCESSFULLY + " " + updatedNotificationDto);
		log.info(StaticStringValues.NOTIFICATION_UPDATED_SUCCESSFULLY);
		return updatedNotificationDto;
	}

	/**
	 * Finds a notification by its ID.
	 * 
	 * @param notificationid The ID of the notification to find.
	 * @return The found NotificationDto.
	 * @throws ResourceNotFoundException if the notification is not found.
	 */
	@Override
	public NotificationDto findByNotificationId(long notificationid) throws ResourceNotFoundException {
		// Log the find request
		log.info(StaticStringValues.REQUEST_NOTIFICATION_DELETEBY_ID + notificationid);

		// Attempt to retrieve the notification
		Optional<Notification> notificationOptional = notificationRepository.findById(notificationid);
		if (notificationOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.NOTIFICATION_DOES_NOT_EXIT_STRING + notificationid);
		}

		// Map the entity to DTO
		NotificationDto notificationDto = notificationOptional.get().mapToNotificationDto();

		// Log the successful find
		log.debug(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY + " " + notificationDto);
		log.info(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY);
		return notificationDto;
	}

	/**
	 * Deletes a notification by its ID.
	 * 
	 * @param notificationId The ID of the notification to delete.
	 * @throws ResourceNotFoundException if the notification is not found.
	 */
	@Override
	public void deleteNotification(long notificationId) throws ResourceNotFoundException {
		// Log the delete request
		log.info(StaticStringValues.REQUEST_NOTIFICATION_DELETEBY_ID + notificationId);

		// Check if the notification exists
		Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
		if (notificationOptional.isEmpty()) {
			// Throw exception if not found
			throw new ResourceNotFoundException(StaticStringValues.NOTIFICATION_DOES_NOT_EXIT_STRING + notificationId);
		}

		// Delete the notification
		notificationRepository.delete(notificationOptional.get());

		// Log the successful deletion
		log.info(StaticStringValues.NOTIFICATION_DELETED_SUCCESSFULLY);
	}
	/**
	 * find all notification.
	 * 
	 */
	@Override
	public List<NotificationDto> findAllNotifications() {
		// Logging the start of the find all notifications request
		log.info(StaticStringValues.REQUEST_FIND_ALL_NOTIFICATIONS);

		// Fetching all notifications from the repository, mapping them to DTOs, and
		// collecting them into a list
		List<NotificationDto> list = notificationRepository.findAll().stream().map(app -> app.mapToNotificationDto())
				.collect(Collectors.toList());

		// Debug logging the successful finding of notifications
		log.debug(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY + " " + list);

		// Logging the successful completion of the find all notifications request
		log.info(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY);

		// Returning the list of all notifications
		return list;
	}
	
	/**
	 * find all notification by specific customer ID.
	 * 
	 * @param customerId The ID of the customer 
	 * @throws ResourceNotFoundException if the customer is not found.
	 */
	@Override
	public List<NotificationDto> findAllByCustomerId(long customerId) throws ResourceNotFoundException {
		// Logging the start of the find all measurements by customer ID request
		log.info(StaticStringValues.REQUEST_FIND_ALL_MEASUREMENTS_CUSTOMERID + customerId);

		// Checking if the customer ID is valid
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		if (customerOptional.isEmpty()) {
			// If the customer ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		// Fetching all notifications for the given customer ID from the repository,
		// mapping them to DTOs, and collecting them into a list
		List<NotificationDto> list = notificationRepository.findAllByCustomer_CustomerId(customerId).stream()
				.map(app -> app.mapToNotificationDto()).collect(Collectors.toList());

		// Debug logging the successful finding of notifications
		log.debug(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY + " " + list);

		// Logging the successful completion of the find all measurements by customer ID
		// request
		log.info(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY);

		// Returning the list of all notifications for the given customer ID
		return list;
	}
	/**
	 * find all notification by specific shop ID.
	 * 
	 * @param shopId The ID of the tailor 
	 * @throws ResourceNotFoundException if the tailor is not found.
	 */
	@Override
	public List<NotificationDto> findAllByShopId(long shopId) throws ResourceNotFoundException {
		// Logging the start of the find all notifications by shop ID request
		log.info(StaticStringValues.REQUEST_FIND_ALL_NOTIFICATIONS_SHOPID + shopId);

		// Checking if the shop ID is valid
		Optional<Tailor> tailorOptional = tailorRepository.findById(shopId);
		if (tailorOptional.isEmpty()) {
			// If the shop ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}

		// Fetching all notifications for the given shop ID from the repository, mapping
		// them to DTOs, and collecting them into a list
		List<NotificationDto> list = notificationRepository.findAllByTailor_ShopId(shopId).stream()
				.map(app -> app.mapToNotificationDto()).collect(Collectors.toList());

		// Debug logging the successful finding of notifications
		log.debug(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY + " " + list);

		// Logging the successful completion of the find all notifications by shop ID
		// request
		log.info(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY);

		// Returning the list of all notifications for the given shop ID
		return list;
	}

	@Override
	public List<NotificationDto> findAllByCustomerIdAndShopId(long customerId, long shopId)
			throws ResourceNotFoundException {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		if (customerOptional.isEmpty()) {
			// If the customer ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.CUSTOMER_DOES_NOT_EXIT_STRING + customerId);
		}

		Optional<Tailor> tailorOptional = tailorRepository.findById(shopId);
		if (tailorOptional.isEmpty()) {
			// If the shop ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}
		List<NotificationDto> list = notificationRepository.findAllByTailor_ShopIdAndCustomer_CustomerId(shopId,customerId).stream()
				.map(app -> app.mapToNotificationDto()).collect(Collectors.toList());

		// Debug logging the successful finding of notifications
		log.debug(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY + " " + list);

		// Logging the successful completion of the find all notifications by shop ID
		// request
		log.info(StaticStringValues.NOTIFICATION_FOUND_SUCCESSFULLY);

		// Returning the list of all notifications for the given shop ID
		return list;

	}

}
