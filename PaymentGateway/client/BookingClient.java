package com.PaymentGateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("/apo/v1/BOOKINGSERVICCE")
public interface BookingClient {
     
	
	@PutMapping("/update-status-booking")
	public boolean updateBooking(@RequestParam long id);
	
}
	

