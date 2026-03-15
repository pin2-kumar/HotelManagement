package com.PropertyService.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.PropertyService.dto.APIResponse;
import com.PropertyService.dto.EmailRequest;
import com.PropertyService.dto.PropertyDto;
import com.PropertyService.dto.RoomsDto;
import com.PropertyService.entity.Area;
import com.PropertyService.entity.City;
import com.PropertyService.entity.Property;
import com.PropertyService.entity.PropertyPhotos;
import com.PropertyService.entity.RoomAvailability;
import com.PropertyService.entity.Rooms;
import com.PropertyService.entity.State;
import com.PropertyService.repository.AreaRepository;
import com.PropertyService.repository.CityRepository;
import com.PropertyService.repository.PropertyPhotosRepository;
import com.PropertyService.repository.PropertyRepository;
import com.PropertyService.repository.RoomAvailabilityRepository;
import com.PropertyService.repository.RoomsRepository;
import com.PropertyService.repository.StateRepository;

@Service
public class PropertyService {

	//@Autowired  // this is remove autowired .at the place of autowired use the constructor
	private PropertyRepository propertyRepository;
	private AreaRepository areaRepository;
	private CityRepository cityRepository;
	private StateRepository stateRepository;
	private PropertyPhotosRepository photosRepository;
	
	private EmailProducer emailProducer;
	
	@Autowired
	private S3Service s3Service;
	
	private RoomAvailabilityRepository availabilityRepository;
	private RoomsRepository roomsRepository;

	

	public PropertyService(PropertyRepository propertyRepository, AreaRepository areaRepository,
			CityRepository cityRepository, StateRepository stateRepository, PropertyPhotosRepository photosRepository,
			S3Service s3Serviec , EmailProducer emailProducer  ,RoomAvailabilityRepository availabilityRepository,
            RoomsRepository roomsRepository) {
		super();
		this.propertyRepository = propertyRepository;
		this.areaRepository = areaRepository;
		this.cityRepository = cityRepository;
		this.stateRepository = stateRepository;
		this.photosRepository = photosRepository;
		this.s3Service = s3Serviec;
		this.emailProducer=emailProducer;
	    this.availabilityRepository = availabilityRepository;
	    this.roomsRepository = roomsRepository;
	}

	public PropertyDto addProperty(PropertyDto dto, MultipartFile[] files) {
	    // Area
	    Area area = areaRepository.findByName(dto.getArea());
	    if (area == null) {
	        area = new Area();
	        area.setName(dto.getArea());
	        area = areaRepository.save(area);
	    }

	    // City
	    City city = cityRepository.findByName(dto.getCity());
	    if (city == null) {
	        city = new City();
	        city.setName(dto.getCity());
	        city = cityRepository.save(city);
	    }

	    // State
	    State state = stateRepository.findByName(dto.getState());
	    if (state == null) {
	        state = new State();
	        state.setName(dto.getState());
	        state = stateRepository.save(state);
	    }

	    // Property
	    Property property = new Property();
	    property.setName(dto.getName());
	    property.setNumberOfBathroom(dto.getNumberOfBathrooms());
	    property.setNumberOfBeds(dto.getNumberOfBeds());
	    property.setNumberOfRooms(dto.getNumberOfRooms());
	    property.setNumberOfGuestAllow(dto.getNumberOfGuestAllowed());
	    property.setArea(area);
	    property.setCity(city);
	    property.setState(state);

	    Property savedProperty = propertyRepository.save(property);

	    // Upload files to S3
	    List<String> fileUrls = s3Service.uploadFiles(files);

	    for (String url : fileUrls) {
	        PropertyPhotos photos = new PropertyPhotos();
	        photos.setUrl(url);
	        photos.setProperty(savedProperty);
	        photosRepository.save(photos);
	        
	        emailProducer.sendEmail(new EmailRequest(
		    	    "pintukumar6206080469@gmail.come",
		    	    "Property added!",
		    	    "Your property has been successfully added."
		    	));

	    }
	    
	    return dto;
	   	}

	   	public APIResponse searchProperty(String city, LocalDate date) {
	   		List<Property> properties = propertyRepository.searchProperty(city,date);
	   		APIResponse<List<Property>> response = new APIResponse<>();
	   		
	   		if(properties.isEmpty()) {
	   		    response.setMessage("No properties found");
	   		} else {
	   		    response.setMessage("properties found");
	   		}

	   		response.setStatus(200);
	   		response.setData(properties);
	   		
	   		return response;
	   	}
	   	
	   	
	   	
	   	public APIResponse<PropertyDto> findPropertyById(long id){
			APIResponse<PropertyDto> response = new APIResponse<>();
			PropertyDto dto  = new PropertyDto();
			Optional<Property> opProp = propertyRepository.findById(id);
			if(opProp.isPresent()) {
				Property property = opProp.get();
				dto.setArea(property.getArea().getName());
				dto.setCity(property.getCity().getName());
				dto.setState(property.getState().getName());
				List<Rooms> rooms = property.getRooms();
				List<RoomsDto> roomsDto = new ArrayList<>();
				for(Rooms room:rooms) {
					RoomsDto roomDto = new RoomsDto();
					BeanUtils.copyProperties(room, roomDto);
					roomsDto.add(roomDto);
				}
				dto.setRooms(roomsDto);
				BeanUtils.copyProperties(property, dto);
				response.setMessage("Matching Record");
				response.setStatus(200);
				response.setData(dto);
				return response;
			}
			
			return null;
		}

		public List<RoomAvailability> getTotalRoomsAvailable(long id) {
			return availabilityRepository.findByRoomId(id);
			
		}
		
		public Rooms getRoomById(long id) {
			return roomsRepository.findById(id).get();
		}

}  	





	   	
//	    // Build response DTO
//	    PropertyDto response = new PropertyDto();
//	    response.setId(savedProperty.getId());
//	    response.setName(savedProperty.getName());
//	    response.setNumberOfBathrooms(savedProperty.getNumberOfBathroom());
//	    response.setNumberOfBeds(savedProperty.getNumberOfBeds());
//	    response.setNumberOfRooms(savedProperty.getNumberOfRooms());
//	    response.setNumberOfGuestAllowed(savedProperty.getNumberOfGuestAllow());
//	    response.setArea(savedProperty.getArea().getName());
//	    response.setCity(savedProperty.getCity().getName());
//	    response.setState(savedProperty.getState().getName());
//	    response.setImageUrls(fileUrls);

	//    return response;
	//}













