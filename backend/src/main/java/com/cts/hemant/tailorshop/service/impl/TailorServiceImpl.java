package com.cts.hemant.tailorshop.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.TailorDto;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.service.TailorService;
import com.cts.hemant.tailorshop.util.StaticStringValues;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TailorServiceImpl implements TailorService {

	@Autowired
	private TailorRepository tailorRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	/**
	 * Creates a new tailor record based on the provided TailorDto.
	 *
	 * @param tailorDto Data transfer object containing tailor details.
	 * @return The created TailorDto with persisted data.
	 * @throws ResourceNotFoundException if the tailor already exists.
	 */
	@Override
	public TailorDto createTailor(TailorDto tailorDto) throws ResourceNotFoundException {
		// Logging the start of the create tailor request
		log.info(StaticStringValues.REQUESTING_TAILOR_CREATE);

		// Checking if the tailor ID is valid
		Optional<Tailor> tailorOptional = tailorRepository.findById(tailorDto.getShopId());
		if (tailorOptional.isPresent()) {
			// If the tailor ID is already present, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_ALREADY_EXIST + tailorDto.getShopId());
		}

		// Checking if the tailor ID is valid
		Optional<Tailor> tailorOptional2 = tailorRepository.findByEmail(tailorDto.getEmail());
		if (tailorOptional2.isPresent()) {
			// If the tailor ID is already present, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_ALREADY_EXIST + tailorDto.getEmail());
		}
		// Check for existing customer with the same email
		if (customerRepository.findByEmail(tailorDto.getEmail()).isPresent()) {
			throw new ResourceNotFoundException(StaticStringValues.EMAIL_ALREADY_REGISTERED_AS_CUSTOMER);
		}

		// Mapping the tailor DTO to a tailor entity
		//encryption and hashing of password
		 String hashPwd = passwordEncoder.encode(tailorDto.getPassword());
		 tailorDto.setPassword(hashPwd);
		 Tailor tailor = tailorDto.mapToTailor();

		// Saving the tailor entity and mapping it back to a tailor DTO
		TailorDto tailorDto2 = tailorRepository.save(tailor).mapToTailorDto();

		// Logging the successful creation of the tailor
		log.debug(StaticStringValues.TAILOR_CREATED_SUCCESSFULLY + " " + tailorDto2);
		log.info(StaticStringValues.TAILOR_CREATED_SUCCESSFULLY);

		// Returning the created tailor DTO
		return tailorDto2;
	}

	/**
	 * Deletes a tailor record identified by the shop ID.
	 *
	 * @param shopId The unique identifier of the tailor to be deleted.
	 * @throws ResourceNotFoundException if the tailor does not exist.
	 */
	@Override
	public void deleteTailor(long shopId) throws ResourceNotFoundException {
		// Logging the start of the delete tailor request
		log.info(StaticStringValues.REQUEST_TAILOR_DELETEBY_ID + shopId);

		// Checking if the tailor ID is valid
		Optional<Tailor> tailorOptional = tailorRepository.findById(shopId);
		if (tailorOptional.isEmpty()) {
			// If the tailor ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}

		// Deleting the tailor entity
		tailorRepository.delete(tailorOptional.get());

		// Logging the successful deletion of the tailor
		log.info(StaticStringValues.TAILOR_DELETED_SUCCESSFULLY);
	}

	/**
	 * Finds a tailor record by its email.
	 *
	 * @param email The email of the tailor to be found.
	 * @return The TailorDto of the found tailor.
	 * @throws ResourceNotFoundException if the tailor does not exist.
	 */
	@Override
	public TailorDto findByEmail(String email) throws ResourceNotFoundException {
		// Logging the start of the find tailor by email request
		log.info(StaticStringValues.REQUEST_TAILOR_FINDBY_EMAIL + email);

		// Checking if the tailor email is valid
		Optional<Tailor> tailorOptional = tailorRepository.findByEmail(email);
		if (tailorOptional.isEmpty()) {
			// If the tailor email is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + email);
		}

		// Mapping the tailor entity to a tailor DTO
		TailorDto tailorDto = tailorOptional.get().mapToTailorDto();

		// Logging the successful finding of the tailor
		log.debug(StaticStringValues.TAILOR_FOUND_SUCCESSFULLY + " " + tailorDto);
		log.info(StaticStringValues.TAILOR_FOUND_SUCCESSFULLY);

		// Returning the TailorDto of the found tailor
		return tailorDto;
	}

	/**
	 * Finds a tailor record by its shop ID.
	 *
	 * @param shopId The unique identifier of the tailor to be found.
	 * @return The TailorDto of the found tailor.
	 * @throws ResourceNotFoundException if the tailor does not exist.
	 */
	@Override
	public TailorDto findByShopId(long shopId) throws ResourceNotFoundException {
		// Logging the start of the find tailor by shop ID request
		log.info(StaticStringValues.REQUEST_TAILOR_FINDBY_ID + shopId);

		// Checking if the tailor shop ID is valid
		Optional<Tailor> tailorOptional = tailorRepository.findById(shopId);
		if (tailorOptional.isEmpty()) {
			// If the tailor shop ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + shopId);
		}

		// Mapping the tailor entity to a tailor DTO
		TailorDto tailorDto = tailorOptional.get().mapToTailorDto();

		// Logging the successful finding of the tailor
		log.debug(StaticStringValues.TAILOR_FOUND_SUCCESSFULLY + " " + tailorDto);
		log.info(StaticStringValues.TAILOR_FOUND_SUCCESSFULLY);

		// Returning the TailorDto of the found tailor
		return tailorDto;
	}

	/**
	 * Finds a tailor record by its name.
	 *
	 * @param tailorName The name of the tailor to be found.
	 * @return The TailorDto of the found tailor.
	 * @throws ResourceNotFoundException if the tailor does not exist.
	 */
	@Override
	public TailorDto findByTailorName(String tailorName) throws ResourceNotFoundException {
		// Logging the start of the find tailor by name request
		log.info(StaticStringValues.REQUEST_TAILOR_FINDBY_NAME + tailorName);

		// Checking if the tailor name is valid
		Optional<Tailor> tailorOptional = tailorRepository.findByTailorName(tailorName);
		if (tailorOptional.isEmpty()) {
			// If the tailor name is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + tailorName);
		}

		// Mapping the tailor entity to a tailor DTO
		TailorDto tailorDto = tailorOptional.get().mapToTailorDto();

		// Logging the successful finding of the tailor
		log.debug(StaticStringValues.TAILOR_FOUND_SUCCESSFULLY + " " + tailorDto);
		log.info(StaticStringValues.TAILOR_FOUND_SUCCESSFULLY);

		// Returning the TailorDto of the found tailor
		return tailorDto;
	}

	/**
	 * Updates an existing tailor record with the details provided in TailorDto.
	 *
	 * @param tailorDto Data transfer object containing updated tailor details.
	 * @return The updated TailorDto with persisted changes.
	 * @throws ResourceNotFoundException if the tailor to be updated does not exist.
	 */
	@Override
	public TailorDto updateTailor(TailorDto tailorDto) throws ResourceNotFoundException {
		// Logging the start of the update tailor request
		log.info(StaticStringValues.REQUEST_TAILOR_UPDATE);

		// Checking if the tailor ID is valid
		Optional<Tailor> tailorOptional = tailorRepository.findById(tailorDto.getShopId());
		if (tailorOptional.isEmpty()) {
			// If the tailor ID is not valid, throwing a resource not found exception
			throw new ResourceNotFoundException(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + tailorDto.getShopId());
		}

		// Getting the tailor entity
		Tailor tailor = tailorOptional.get();

		// Updating the tailor entity with the details provided in the tailor DTO
//		if (tailorDto.getEmail() != null) {
//			tailor.setEmail(tailorDto.getEmail());
//		}
		if (tailorDto.getCoverUrl() != null) {
			tailor.setCoverUrl(tailor.getCoverUrl());
		}
		if (tailorDto.getDescription() != null) {
			tailor.setDescription(tailorDto.getDescription());
		}
//		if (tailorDto.getPassword() != null) {
//			tailor.setPassword(tailorDto.getPassword());
//		}

		// Saving the updated tailor entity and mapping it back to a tailor DTO
		TailorDto updatedDto = tailorRepository.save(tailor).mapToTailorDto();

		// Logging the successful update of the tailor
		log.debug(StaticStringValues.TAILOR_UPDATED_SUCCESSFULLY + " " + updatedDto);
		log.info(StaticStringValues.TAILOR_UPDATED_SUCCESSFULLY);

		// Returning the updated tailor DTO
		return updatedDto;
	}

	/**
	 * Retrieves all tailor records.
	 *
	 * @return A list of TailorDto objects representing all tailors.
	 */
	@Override
	public List<TailorDto> findAllTailors() {
		// Logging the start of the find all tailors request
		log.info(StaticStringValues.REQUEST_FIND_ALL_TAILORS);

		// Fetching all tailors from the repository, mapping them to DTOs, and
		// collecting them into a list
		List<TailorDto> tailorDto = tailorRepository.findAll().stream().map(pay -> pay.mapToTailorDto())
				.collect(Collectors.toList());

		// Logging the successful finding of tailors
		log.debug(StaticStringValues.TAILOR_FOUND_SUCCESSFULLY + " " + tailorDto);
		log.info(StaticStringValues.TAILOR_FOUND_SUCCESSFULLY);

		// Returning a list of TailorDto objects representing all tailors
		return tailorDto;
	}

}
