package com.cts.hemant.tailorshop.payload;

import com.cts.hemant.tailorshop.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

	private long notificationId;
	
	private String message;
	
	private long customerId;
	
	private long shopId;
	
	public Notification mapToNotification() {
		return new Notification(notificationId, message, null, null);
	}
}
