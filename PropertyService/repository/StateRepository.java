package com.PropertyService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PropertyService.entity.State;

public interface StateRepository extends JpaRepository<State, Long>  {
	State findByName(String name);
	}
