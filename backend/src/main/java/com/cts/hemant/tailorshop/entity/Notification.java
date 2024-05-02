package com.cts.hemant.tailorshop.entity;

import com.cts.hemant.tailorshop.payload.NotificationDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private long notificationId;
	
	@Column(name = "message", nullable = false)
	private String message;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="shop_id")
	private Tailor tailor;
	
	public NotificationDto mapToNotificationDto() {
		return new NotificationDto(notificationId, message, customer.getCustomerId(), tailor.getShopId());
	}

}
