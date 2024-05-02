package com.cts.hemant.tailorshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.hemant.tailorshop.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	/**
	 * This method is used to find all Notification entities associated with a
	 * specific Customer based on the customerId.
	 * 
	 * @param customerId This is the ID of the customer whose notifications are to
	 *                   be found.
	 * @return List<Notification> This returns the list of found notifications.
	 */
	public List<Notification> findAllByCustomer_CustomerId(long customerId);

	/**
	 * This method is used to find all Notification entities associated with a
	 * specific Tailor based on the shopId.
	 * 
	 * @param shopId This is the ID of the shop whose notifications are to be found.
	 * @return List<Notification> This returns the list of found notifications.
	 */
	public List<Notification> findAllByTailor_ShopId(long shopId);
	
	
	public List<Notification> findAllByTailor_ShopIdAndCustomer_CustomerId(long shopId, long customerId);
	
	
	
	
}