package com.cts.hemant.tailorshop.entity;

import com.cts.hemant.tailorshop.payload.TailorDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Tailor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shop_id")
	private long shopId;
	@Column(name = "tailor_name", nullable = false)
	private String tailorName;
	@Column(name = "tailor_description")
	private String description;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	
	private String coverUrl;
//	
//	@OneToMany
//	@JoinColumn
//	private List<Appointment> appointments;

	public TailorDto mapToTailorDto() {
		return new TailorDto(shopId, tailorName, description, email, null, coverUrl);
	}

}
