package com.cts.hemant.tailorshop.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.hemant.tailorshop.entity.Customer;
import com.cts.hemant.tailorshop.entity.Tailor;
import com.cts.hemant.tailorshop.exception.ResourceNotFoundException;
import com.cts.hemant.tailorshop.payload.TailorDto;
import com.cts.hemant.tailorshop.repository.CustomerRepository;
import com.cts.hemant.tailorshop.repository.TailorRepository;
import com.cts.hemant.tailorshop.util.StaticStringValues;

@ExtendWith(MockitoExtension.class)
class TailorServiceImplTest {

	@Mock
	private TailorRepository tailorRepository;
	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private TailorServiceImpl tailorService;

	private final Tailor TAILOR_1 = new Tailor(1L, "Kashif Umar", "Hello How are You doing", "Kashif@gmail.com",
			"1232323", null),
			TAILOR_2 = new Tailor(2L, "Kashif Umar", "Hello How are You doing", "Kashif@gmail.com", "1232323", null);
	private final TailorDto TAILOR_DTO_1 = new TailorDto(1L, "Kashif Umar", "Hello How are You doing",
			"Kashif@gmail.com", "1232323", null);

	@Test
	void testCreateTailor() throws ResourceNotFoundException {
		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());
		when(tailorRepository.save(TAILOR_1)).thenReturn(TAILOR_1);

		TailorDto tailorDto = tailorService.createTailor(TAILOR_DTO_1);

		verify(tailorRepository, times(1)).save(TAILOR_1);
		assertNotNull(tailorDto);
		assertThat(tailorDto.getShopId()).isEqualTo(TAILOR_DTO_1.getShopId());

	}

	@Test
	void testCreateTailor_whenInvalidShopId() {
		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			tailorService.createTailor(TAILOR_DTO_1);
		});

		verify(tailorRepository, never()).save(TAILOR_1);

		assertThat(exception.getMessage().equals(StaticStringValues.TAILOR_ALREADY_EXIST + 1));

	}
	@Test
	void testCreateTailor_whenInvalidEmail() {
		when(tailorRepository.findByEmail(TAILOR_DTO_1.getEmail())).thenReturn(Optional.of(TAILOR_1));

		assertThrows(ResourceNotFoundException.class, () -> {
			tailorService.createTailor(TAILOR_DTO_1);
		});

		verify(tailorRepository, never()).save(TAILOR_1);


	}
	@Test
	void testCreateTailor_whenInvalidSEmail() {
		when(customerRepository.findByEmail(TAILOR_DTO_1.getEmail())).thenReturn(Optional.of(new Customer()));

		assertThrows(ResourceNotFoundException.class, () -> {
			tailorService.createTailor(TAILOR_DTO_1);
		});

		verify(tailorRepository, never()).save(TAILOR_1);


	}

	@Test
	void testDeleteTailor()throws ResourceNotFoundException  {

		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));
		doNothing().when(tailorRepository).delete(TAILOR_1);

		tailorService.deleteTailor(1L);

		verify(tailorRepository, times(1)).findById(1L);
		verify(tailorRepository, times(1)).delete(TAILOR_1);

	}

	@Test
	void testDeleteTailor_whenInvalidShopId() {
		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			tailorService.deleteTailor(1L);
		});

		verify(tailorRepository, never()).delete(TAILOR_1);

		assertThat(exception.getMessage().equals(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + 1));

	}

	@Test
	void testFindByEmail() throws ResourceNotFoundException {
		when(tailorRepository.findByEmail("Kashif@gmail.com")).thenReturn(Optional.of(TAILOR_1));

		TailorDto tailorDto = tailorService.findByEmail("Kashif@gmail.com");

		verify(tailorRepository, times(1)).findByEmail("Kashif@gmail.com");

		assertNotNull(tailorDto);
		assertThat(tailorDto.getShopId()).isEqualTo(1L);

	}

	@Test
	void testFindByEmail_whenInvalidEmail() {
		when(tailorRepository.findByEmail("Kashif@gmail.com")).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			tailorService.findByEmail("Kashif@gmail.com");
		});

		verify(tailorRepository, times(1)).findByEmail("Kashif@gmail.com");

		assertThat(exception.getMessage().equals(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + "Kashif@gmail.com"));

	}

	@Test
	void testFindByShopId() throws ResourceNotFoundException {
		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));

		TailorDto tailorDto = tailorService.findByShopId(1L);

		verify(tailorRepository, times(1)).findById(1L);

		assertNotNull(tailorDto);
		assertThat(tailorDto.getShopId()).isEqualTo(1L);
	}

	@Test
	void testFindByShopId_whenInvalidShopId() {
		final String expectedMessage = "Tailor id doesn't present.1";
		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			tailorService.findByShopId(1L);
		});

		assertThat(exception.getMessage().equals(expectedMessage));

	}

	@Test
	void testFindByTailorName() throws ResourceNotFoundException {
		when(tailorRepository.findByTailorName("Kashif Umar")).thenReturn(Optional.of(TAILOR_1));

		TailorDto tailorDto = tailorService.findByTailorName("Kashif Umar");

		verify(tailorRepository, times(1)).findByTailorName("Kashif Umar");

		assertNotNull(tailorDto);
		assertThat(tailorDto.getShopId()).isEqualTo(1L);
	}

	@Test
	void testFindByTailorName_whenInvalidTailorName() {
		when(tailorRepository.findByTailorName("Kashif Umar")).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			tailorService.findByTailorName("Kashif Umar");
		});

		verify(tailorRepository, times(1)).findByTailorName("Kashif Umar");

		assertThat(exception.getMessage().equals(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + "Kashif@gmail.com"));

	}

	@Test
	void testUpdateTailor() throws ResourceNotFoundException {
		when(tailorRepository.findById(1L)).thenReturn(Optional.of(TAILOR_1));

		when(tailorRepository.save(TAILOR_1)).thenReturn(TAILOR_1);

		TailorDto tailorDto = tailorService.updateTailor(TAILOR_DTO_1);

		verify(tailorRepository, times(1)).save(TAILOR_1);
		assertNotNull(tailorDto);
		assertThat(tailorDto.getShopId()).isEqualTo(TAILOR_DTO_1.getShopId());

	}

	@Test
	void testUpdateTailor_whenInvalidShopId() {
		when(tailorRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			tailorService.updateTailor(TAILOR_DTO_1);
		});
		verify(tailorRepository, never()).save(TAILOR_1);

		assertThat(exception.getMessage().equals(StaticStringValues.TAILOR_DOES_NOT_EXIT_STRING + 1));

	}

	@Test
	void testFindAllTailors() {
		when(tailorRepository.findAll()).thenReturn(Arrays.asList(TAILOR_1, TAILOR_2));

		List<TailorDto> list = tailorService.findAllTailors();

		assertNotNull(list);
		assertThat(list.size()).isEqualTo(2);
	}

}
