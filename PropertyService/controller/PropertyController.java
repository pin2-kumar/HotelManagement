package com.PropertyService.controller;

import com.PropertyService.dto.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.PropertyService.dto.PropertyDto;
import com.PropertyService.entity.RoomAvailability;
import com.PropertyService.entity.Rooms;
import com.PropertyService.repository.RoomAvailabilityRepository;
import com.PropertyService.service.PropertyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
	
	@Autowired
	private RoomAvailabilityRepository availabilityRepository;
	
	private PropertyService propertyService;

	public PropertyController(PropertyService propertyService) {
		
		this.propertyService = propertyService;
	}

	           // http://localhost:9091/api/v1/property/add-property
	
	
	@PostMapping(
		    value = "/add-property",
		    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,  // Ensures the endpoint accepts multipart/form-data
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<APIResponse> addProperty(
		        @RequestParam("property") String propertyJson,  // Use RequestParam to get the property as a raw JSON string
		        @RequestParam("files") MultipartFile[] files) {  // Use RequestParam to handle files


		ObjectMapper objectMapper = new ObjectMapper();
		PropertyDto dto = null;
		
		try {
			dto = objectMapper.readValue(propertyJson, PropertyDto.class);
			
		}catch (JsonProcessingException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
	
		PropertyDto property = propertyService.addProperty(dto,files);
		
		APIResponse<PropertyDto> response = new APIResponse<>();
		response.setMessage("property added");
		response.setStatus(201);
		response.setData(property);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/search-property")
	
	//  http://localhost:9091/api/v1/property/search-property?name=SomeName&date=2025-08-25
	//   http://localhost:9091/api/v1/property/search-property?name=BTM&date=2025-05-02
	
	public APIResponse searchProperty(
	        @RequestParam String name,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
	    APIResponse response = propertyService.searchProperty(name, date);
	    return response;

}

	
	@GetMapping("/property-id")
	public APIResponse<PropertyDto> getPropertyById(@RequestParam long id){
		APIResponse<PropertyDto> response = propertyService.findPropertyById(id);
		return response;
	}
	
	
	
	@GetMapping("/room-available-room-id")
	public APIResponse<List<RoomAvailability>> getTotalRoomsAvailable(@RequestParam long id){
		List<RoomAvailability> totalRooms = propertyService.getTotalRoomsAvailable(id);
		
		APIResponse<List<RoomAvailability>> response = new APIResponse<>();
	    response.setMessage("Total rooms");
	    response.setStatus(200);
	    response.setData(totalRooms);
	    return response;
	}
	
	
	
	@GetMapping("/room-id")
	public APIResponse<Rooms> getRoomType(@RequestParam long id){
		Rooms room = propertyService.getRoomById(id);
		
		APIResponse<Rooms> response = new APIResponse<>();
	    response.setMessage("Total rooms");
	    response.setStatus(200);
	    response.setData(room);
	    return response;
	}
	
	@PutMapping("/updateRoomCount")
	public APIResponse<Boolean> updateRoomCount(@RequestParam long id, @RequestParam LocalDate date) {
	    APIResponse<Boolean> response = new APIResponse<>();

	    // ✅ Correct repository method
	    RoomAvailability roomsAvailable = availabilityRepository.findByRoomIdAndAvailableDate(id, date);

	    if (roomsAvailable == null) {
	        response.setMessage("No rooms found for given id and date");
	        response.setStatus(404);
	        response.setData(false);
	        return response;
	    }

	    int count = roomsAvailable.getAvailableCount();
	    if (count > 0) {
	        roomsAvailable.setAvailableCount(count - 1);
	        availabilityRepository.save(roomsAvailable);

	        response.setMessage("updated");
	        response.setStatus(200);
	        response.setData(true);
	    } else {
	        response.setMessage("No Availability");
	        response.setStatus(500);
	        response.setData(false);
	    }

	    return response;
	}

	 
}





