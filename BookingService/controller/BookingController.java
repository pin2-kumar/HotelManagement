package com.BookingService.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BookingService.client.PropertyClient;
import com.BookingService.dto.BookingDto;
import com.BookingService.dto.RoomAvailability;
import com.BookingService.dto.Rooms;
import com.BookingService.entity.Booking;
import com.BookingService.entity.BookingDate;
import com.BookingService.repository.BookingDateRepository;
import com.BookingService.repository.BookingRepository;
import com.PaymentGateway.dto.ProductRequest;
import com.BookingService.dto.PropertyDto;
import com.PaymentGateway.dto.StripeResponse;


import com.BookingService.dto.APIResponse;


@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

	
	@Autowired
	private PropertyClient propertyClient;
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private BookingDateRepository bookingDateRepository;
	
//	@Autowired
//	private ProductRequest productRequest;
//	
//	

	
	@PostMapping("/add-to-cart")
	
	//      http://localhost:8085/api/v1/booking/add-to-card
	
	public APIResponse<List<String>> cart(@RequestBody BookingDto bookingDto) {
		
		Optional<RoomAvailability>matchedRoom = java.util.Optional.empty();
		APIResponse<List<String>> apiResponse = new APIResponse<>();
		
		List<String> messages = new ArrayList<>();
		
		APIResponse<PropertyDto> response = propertyClient.getPropertyById(bookingDto.getPropertyId());

		APIResponse<Rooms> roomType = propertyClient.getRoomType(bookingDto.getRoomId());
		
		APIResponse<List<RoomAvailability>> totalRoomsAvailable = propertyClient.getTotalRoomsAvailable(bookingDto.getRoomAvailabilityId());
		
		List<RoomAvailability> availableRooms = totalRoomsAvailable.getData();
		
		//Logic to check available rooms based on date and count
		for(LocalDate date: bookingDto.getDate()) {
			boolean isAvailable = availableRooms.stream()
			        .anyMatch(ra -> ra.getAvailableDate().equals(date) && ra.getAvailableCount()>0);
			
			    
			    System.out.println("Date " + date + " available: " + isAvailable);
			    
			    if (!isAvailable) {
			    	 messages.add("Room not available on: " + date);
			    	 apiResponse.setMessage("Sold Out");
			 		 apiResponse.setStatus(500);
			 		 apiResponse.setData(messages);
			 		 return apiResponse;
			    }
			    
			    matchedRoom = availableRooms.stream()
			            .filter(ra -> ra.getAvailableDate().equals(date) && ra.getAvailableCount() > 0)
			            .findFirst();

			    
//			    if (matchedRoom.isPresent()) {
//			    	Long id = matchRoom.get().getId();
//			    }
		}
		//Save it to Booking Table with status pending
		Booking booking = new Booking();
		booking.setName(bookingDto.getName());
		booking.setEmail(bookingDto.getEmail());
		booking.setMobile(bookingDto.getMobile());
		booking.setPropertyName(response.getData().getName());
		booking.setStatus("pending");
		booking.setTotalPrice(roomType.getData().getBasePrice()*bookingDto.getTotalNights());
		Booking savedBooking = bookingRepository.save(booking);
		
		for(LocalDate date: bookingDto.getDate()) {
			BookingDate  bookingDate = new BookingDate();
			bookingDate.setDate(date);
			bookingDate.setBooking(savedBooking);
			BookingDate saveBookingDate = bookingDateRepository.save(bookingDate);
		
			if (saveBookingDate != null) {
			 propertyClient.updateRoomCount(matchedRoom.get().getId(), date);
			 
		 }
		}
			
		return apiResponse;
	}
	
	@PostMapping("/process-payment")
	public StripeResponse processPayment(@RequestBody ProductRequest productRequest) {
		StripeResponse stripeResponse = propertyClient.checkoutProduct(productRequest);
		return stripeResponse;
	}
	
	@PutMapping("/update-status-booking")
	public boolean updateBooking(@RequestParam long id) {
		Optional<Booking> opBooking = bookingRepository.findById(id);
		if (opBooking.isPresent()) {
		Booking bookings = opBooking.get();
		bookings.setStatus("confirmed");
		Booking savedBooking = bookingRepository.save(bookings);  // save db me

        if (savedBooking != null) {
			return true;
			
		}
		
	}
	return false;

}

}

















