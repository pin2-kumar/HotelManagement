package com.BookingService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BookingService.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}


