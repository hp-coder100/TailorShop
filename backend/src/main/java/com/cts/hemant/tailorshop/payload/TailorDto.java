package com.cts.hemant.tailorshop.payload;

import com.cts.hemant.tailorshop.entity.Tailor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TailorDto {
	
	private long shopId;
	private String tailorName;
	private String description;
	private String email;
	private String password;
	private String coverUrl;
	
	public Tailor mapToTailor() {
		return new Tailor(shopId, tailorName, description, email, password, coverUrl);
	}
}
