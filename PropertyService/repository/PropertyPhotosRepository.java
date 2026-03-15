package com.PropertyService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PropertyService.entity.PropertyPhotos;

public interface PropertyPhotosRepository extends JpaRepository<PropertyPhotos,Long> {

	PropertyPhotos save(PropertyPhotos photos);

}
