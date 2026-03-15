package com.BookingService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BookingService.entity.BookingDate;

public interface BookingDateRepository extends JpaRepository<BookingDate, Long> {

}
