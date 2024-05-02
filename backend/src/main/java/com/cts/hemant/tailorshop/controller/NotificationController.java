package com.cts.hemant.tailorshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;

import com.cts.hemant.tailorshop.payload.NotificationDto;
import com.cts.hemant.tailorshop.service.NotificationService;

/**
 * Controller class for handling notification-related operations.
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	/**
	 * This method is used to create a new notification.
	 * 
	 * @param notificatoinDto This is the notification data to be created.
	 * @return ResponseEntity This returns the created notification.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   notification to be created is not found.
	 */
	@PostMapping("/create")
	public ResponseEntity<Object> createNotification(@RequestBody NotificationDto notificatoinDto)
			throws ResourceNotFoundException {

		NotificationDto notificatoinDto2 = notificationService.createNotification(notificatoinDto);
		return new ResponseEntity<>(notificatoinDto2, HttpStatus.CREATED);

	}

	/**
	 * This method is used to get all notifications or filter notifications by shop
	 * ID or customer ID.
	 * 
	 * @param shopId     This is the ID of the shop whose notifications are to be
	 *                   found. If not provided, all notifications are found.
	 * @param customerId This is the ID of the customer whose notifications are to
	 *                   be found. If not provided, all notifications are found.
	 * @return ResponseEntity This returns the list of found notifications.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   notifications are not found.
	 */
	@GetMapping("/findAll")
	public ResponseEntity<Object> findAllNotifications(@RequestParam(value = "shopId") long shopId,
			@RequestParam(value = "customerId") long customerId) throws ResourceNotFoundException {
		Object notificationDtos = null;

		notificationDtos = notificationService.findAllByCustomerIdAndShopId(customerId, shopId);

		return new ResponseEntity<>(notificationDtos, HttpStatus.OK);

	}

	/**
	 * This method is used to update a notification.
	 * 
	 * @param notificatoinDto This is the notification data to be updated.
	 * @return ResponseEntity This returns the updated notification.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   notification to be updated is not found.
	 */
	@PutMapping("/update")
	public ResponseEntity<Object> updateNotification(@RequestBody NotificationDto notificatoinDto)
			throws ResourceNotFoundException {
		NotificationDto notificatoinDto2 = notificationService.updateNotification(notificatoinDto);
		return new ResponseEntity<>(notificatoinDto2, HttpStatus.OK);

	}

	/**
	 * This method is used to get a notification by its ID.
	 * 
	 * @param notificationId This is the ID of the notification to be found.
	 * @return ResponseEntity This returns the found notification.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   notification is not found.
	 */
	@GetMapping("/find/{notificationId}")
	public ResponseEntity<Object> findByNotificationId(@PathVariable("notificationId") long notificationId)
			throws ResourceNotFoundException {
		NotificationDto notificationDto = notificationService.findByNotificationId(notificationId);
		return new ResponseEntity<>(notificationDto, HttpStatus.OK);

	}

	/**
	 * This method is used to delete a notification by its ID.
	 * 
	 * @param notificationId This is the ID of the notification to be deleted.
	 * @return ResponseEntity This returns a success message after deletion.
	 * @throws ResourceNotFoundException This exception is thrown when the
	 *                                   notification to be deleted is not found.
	 */
	@DeleteMapping("/delete/{notificationId}")
	public ResponseEntity<Object> deleteNotification(@PathVariable("notificationId") long notificationId)
			throws ResourceNotFoundException {
		notificationService.deleteNotification(notificationId);
		return new ResponseEntity<>("Notification deleted Successfully", HttpStatus.OK);

	}
}
