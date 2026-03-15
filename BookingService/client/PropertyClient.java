package com.BookingService.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.BookingService.dto.RoomAvailability;
import com.BookingService.dto.PropertyDto;
import com.BookingService.dto.APIResponse;
import com.BookingService.dto.Rooms;
import com.PaymentGateway.dto.ProductRequest;
import com.PaymentGateway.dto.StripeResponse;



@FeignClient(name = "PROPERTYSERVICE") 
public interface PropertyClient {
	
	@GetMapping("/api/v1/property/property-id")
	public APIResponse<PropertyDto> getPropertyById(@RequestParam long id);
	
	@GetMapping("/api/v1/property/room-available-room-id")
	public APIResponse<List<RoomAvailability>> getTotalRoomsAvailable(@RequestParam long id);


	@GetMapping("/api/v1/property/room-id")
	public APIResponse<com.BookingService.dto.Rooms> getRoomType(@RequestParam long id);

	
	@PutMapping ("/updateRoomCount")
	public APIResponse<Boolean> updateRoomCount(@RequestParam long id , @RequestParam LocalDate date);


	@PostMapping("/api/v1/property/checkout")
	StripeResponse checkoutProduct(@RequestBody ProductRequest productRequest);

	          

}