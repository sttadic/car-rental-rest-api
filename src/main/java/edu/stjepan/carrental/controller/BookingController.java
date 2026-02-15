package edu.stjepan.carrental.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import edu.stjepan.carrental.dto.*;
import edu.stjepan.carrental.service.BookingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;

	@Autowired
	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping
	public BookingDTO createBooking(@Valid @RequestBody CreateBookingRequest request) {
		return bookingService.createBooking(request);
	}

	@GetMapping("/{id}")
	public BookingDTO getBooking(@PathVariable Long id) {
		return bookingService.getBookingById(id);
	}

	@GetMapping
	public Page<BookingDTO> getBookings(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		return bookingService.getAllBookings(PageRequest.of(page, size));
	}

	@GetMapping("/car/{carId}")
	public List<BookingDTO> getBookingsForCar(@PathVariable Long carId) {
		return bookingService.getBookingsForCar(carId);
	}

	@GetMapping("/date-range")
	public List<BookingDTO> getBookingByDateRange(@RequestParam LocalDate start, @RequestParam LocalDate end) {
		return bookingService.getBookingsByDateRange(start, end);
	}

	@DeleteMapping("/{id}")
	public void deleteBooking(@PathVariable Long id) {
		bookingService.deleteBooking(id);
	}
}
