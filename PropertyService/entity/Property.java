package com.PropertyService.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Property")
public class Property {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private long id ;
	private String name;
	
	@Column(name="number_of_Beds")
	private int numberOfBeds;
	
	@Column (name="number_of_Rooms")
	private int numberOfRooms;
	
	@Column(name="number_of_Bathroom")
	private int numberOfBathroom;
	
	@Column(name="number_of_GuestAllowed")
	private int numberOfGuestAllow;
	
	@ManyToOne
	@JoinColumn(name="area_id")
	private Area area;
	
	@ManyToOne
	@JoinColumn(name="city_id")
	private City city;
	
	@ManyToOne
	@JoinColumn(name="state")
	private State state;
	
	
	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
    private List<Rooms> rooms = new ArrayList<>();


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getNumberOfBeds() {
		return numberOfBeds;
	}


	public void setNumberOfBeds(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}


	public int getNumberOfRooms() {
		return numberOfRooms;
	}


	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}


	public int getNumberOfBathroom() {
		return numberOfBathroom;
	}


	public void setNumberOfBathroom(int numberOfBathroom) {
		this.numberOfBathroom = numberOfBathroom;
	}


	public int getNumberOfGuestAllow() {
		return numberOfGuestAllow;
	}


	public void setNumberOfGuestAllow(int numberOfGuestAllow) {
		this.numberOfGuestAllow = numberOfGuestAllow;
	}


	public Area getArea() {
		return area;
	}


	public void setArea(Area area) {
		this.area = area;
	}


	public City getCity() {
		return city;
	}


	public void setCity(City city) {
		this.city = city;
	}


	public State getState() {
		return state;
	}


	public void setState(State state) {
		this.state = state;
	}


	public List<Rooms> getRooms() {
		return rooms;
	}


	public void setRooms(List<Rooms> rooms) {
		this.rooms = rooms;
	}
	
	
	
	
}
