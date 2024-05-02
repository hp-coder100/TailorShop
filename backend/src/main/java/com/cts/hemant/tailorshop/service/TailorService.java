package com.cts.hemant.tailorshop.service;

import java.util.List;

import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.TailorDto;

public interface TailorService {

	public TailorDto createTailor(TailorDto tailorDto) throws ResourceNotFoundException;

	public void deleteTailor(long tailorId) throws ResourceNotFoundException;

	public TailorDto findByEmail(String email) throws ResourceNotFoundException;

	public TailorDto findByShopId(long shopId) throws ResourceNotFoundException;

	public TailorDto findByTailorName(String tailorName) throws ResourceNotFoundException;

	public TailorDto updateTailor(TailorDto tailorDto) throws ResourceNotFoundException;

	public List<TailorDto> findAllTailors();
}
