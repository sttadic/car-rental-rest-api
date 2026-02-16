package edu.stjepan.carrental.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import edu.stjepan.carrental.dto.*;
import edu.stjepan.carrental.entity.*;
import edu.stjepan.carrental.mapper.BookingMapper;
import edu.stjepan.carrental.repository.*;
import edu.stjepan.carrental.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {
	private final BookingRepository bookingRepository;
	private final CarRepository carRepository;

	@Autowired
	public BookingServiceImpl(BookingRepository bookingRepository, CarRepository carRepository) {
		this.bookingRepository = bookingRepository;
		this.carRepository = carRepository;
	}

	@Override
	public BookingDTO createBooking(CreateBookingRequest request) {
		Car car = carRepository.findById(request.getCarId())
				.orElseThrow(() -> new IllegalArgumentException("Car not found with id: " + request.getCarId()));

		if (request.getEndDate().isBefore(request.getStartDate())) {
			throw new IllegalArgumentException("End date cannot be before start date.");
		}

		Booking booking = new Booking(car, request.getCustomerName(), request.getCustomerEmail(),
				request.getStartDate(), request.getEndDate(), request.getTotalAmount(), request.getStatus());

		Booking saved = bookingRepository.save(booking);
		return BookingMapper.toDTO(saved);
	}

	@Override
	public BookingDTO getBookingById(Long id) {
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
		return BookingMapper.toDTO(booking);
	}

	@Override
	public List<BookingDTO> getBookingsForCar(Long carId) {
		return bookingRepository.findByCarId(carId).stream().map(BookingMapper::toDTO).toList();
	}

	@Override
	public Page<BookingDTO> getAllBookings(Pageable pageable) {
		return bookingRepository.findAll(pageable).map(BookingMapper::toDTO);
	}

	@Override
	public List<BookingDTO> getBookingsByDateRange(LocalDate start, LocalDate end) {
		return bookingRepository.findByStartDateBetween(start, end).stream().map(BookingMapper::toDTO).toList();
	}

	@Override
	public void deleteBooking(Long id) {
		if (!bookingRepository.existsById(id)) {
			throw new IllegalArgumentException("Booking not found with id: " + id);
		}
		bookingRepository.deleteById(id);

	}

}
