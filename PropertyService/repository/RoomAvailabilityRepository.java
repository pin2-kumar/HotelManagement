package com.PropertyService.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PropertyService.entity.RoomAvailability;


public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability , Long> {

	public List<RoomAvailability> findByRoomId(long id);

	public RoomAvailability findByRoomIdAndAvailableDate(long id, LocalDate date);


}



