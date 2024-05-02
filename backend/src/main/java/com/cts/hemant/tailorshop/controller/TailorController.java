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
import com.cts.hemant.tailorshop.payload.TailorDto;
import com.cts.hemant.tailorshop.service.TailorService;

/**
 * Controller class for handling tailor-related operations.
 */
@RestController
@RequestMapping("/tailor")
public class TailorController {

	@Autowired
	private TailorService tailorService;

	/**
	 * This method is used to create a new tailor.
	 * 
	 * @param tailorDto This is the tailor data to be created.
	 * @return ResponseEntity This returns the created tailor.
	 * @throws ResourceNotFoundException This exception is thrown when the tailor to
	 *                                   be created is not found.
	 */
	@PostMapping("/create")
	public ResponseEntity<Object> createTailor(@RequestBody TailorDto tailorDto) throws ResourceNotFoundException {
		TailorDto tailorDto2 = tailorService.createTailor(tailorDto);
		return new ResponseEntity<>(tailorDto2, HttpStatus.CREATED);
	}

	/**
	 * This method is used to get all tailors or filter tailors by email or tailor
	 * name.
	 * 
	 * @param email      This is the email of the tailor to be found. If not
	 *                   provided, all tailors are found.
	 * @param tailorName This is the name of the tailor to be found. If not
	 *                   provided, all tailors are found.
	 * @return ResponseEntity This returns the list of found tailors.
	 * @throws ResourceNotFoundException This exception is thrown when the tailors
	 *                                   are not found.
	 */
	@GetMapping("/findAll")
	public ResponseEntity<Object> findAllTailors(
			@RequestParam(value = "tailorName", required = false, defaultValue = "NotRequired") String tailorName)
			throws ResourceNotFoundException {
		Object tailorDtoDtos = null;

		if (!tailorName.equals("NotRequired")) {
			tailorDtoDtos = tailorService.findByTailorName(tailorName);
		} else {
			tailorDtoDtos = tailorService.findAllTailors();
		}
		return new ResponseEntity<>(tailorDtoDtos, HttpStatus.OK);
	}

	/**
	 * This method is used to update a tailor.
	 * 
	 * @param tailorDto This is the tailor data to be updated.
	 * @return ResponseEntity This returns the updated tailor.
	 * @throws ResourceNotFoundException This exception is thrown when the tailor to
	 *                                   be updated is not found.
	 */
	@PutMapping("/update")
	public ResponseEntity<Object> updateTailor(@RequestBody TailorDto tailorDto) throws ResourceNotFoundException {
		TailorDto tailorDto2 = tailorService.updateTailor(tailorDto);
		return new ResponseEntity<>(tailorDto2, HttpStatus.OK);
	}

	/**
	 * This method is used to get a tailor by shop ID.
	 * 
	 * @param shopId This is the ID of the shop whose tailor is to be found.
	 * @return ResponseEntity This returns the found tailor.
	 * @throws ResourceNotFoundException This exception is thrown when the tailor is
	 *                                   not found.
	 */
	@GetMapping("/find/{shopId}")
	public ResponseEntity<Object> findByShopId(@PathVariable("shopId") long shopId) throws ResourceNotFoundException {
		TailorDto tailorDtoDto = tailorService.findByShopId(shopId);
		return new ResponseEntity<>(tailorDtoDto, HttpStatus.OK);
	}

	/**
	 * This method is used to delete a tailor by shop ID.
	 * 
	 * @param shopId This is the ID of the shop whose tailor is to be deleted.
	 * @return ResponseEntity This returns a success message after deletion.
	 * @throws ResourceNotFoundException This exception is thrown when the tailor to
	 *                                   be deleted is not found.
	 */
	@DeleteMapping("/delete/{shopId}")
	public ResponseEntity<Object> deleteTailor(@PathVariable("shopId") long shopId) throws ResourceNotFoundException {
		tailorService.deleteTailor(shopId);
		return new ResponseEntity<>("Tailor deleted Successfully", HttpStatus.OK);
	}

}
