package com.PropertyService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PropertyService.entity.Rooms;

public interface RoomsRepository extends JpaRepository<Rooms,Long> {

	
}
