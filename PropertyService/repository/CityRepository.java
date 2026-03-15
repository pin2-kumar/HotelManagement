package com.PropertyService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PropertyService.entity.City;

public interface CityRepository extends JpaRepository<City,Long> {

	City findByName(String name);
}
