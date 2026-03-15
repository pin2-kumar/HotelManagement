package com.PropertyService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PropertyService.entity.Area;

public interface AreaRepository extends JpaRepository<Area ,Long> {

      Area findByName(String name);
}
