package com.cts.hemant.tailorshop.service;

import java.util.List;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.NotificationDto;

public interface NotificationService {

	public NotificationDto createNotification(NotificationDto notificationDto) throws ResourceNotFoundException;

	public NotificationDto updateNotification(NotificationDto notificationDto)  throws ResourceNotFoundException;

	public NotificationDto findByNotificationId(long notificationid) throws ResourceNotFoundException;

	public void deleteNotification(long notificationId) throws ResourceNotFoundException;
	
	public List<NotificationDto> findAllNotifications();

	public List<NotificationDto> findAllByCustomerId(long customerId) throws ResourceNotFoundException;

	public List<NotificationDto> findAllByShopId(long shopId) throws ResourceNotFoundException;
	
	
	public List<NotificationDto> findAllByCustomerIdAndShopId(long customerId, long shopId) throws ResourceNotFoundException;

}


