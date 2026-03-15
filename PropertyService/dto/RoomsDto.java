package com.PropertyService.dto;

public class RoomsDto {

	private long id;
	private String roomstype;
	private double basePrice;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRoomstype() {
		return roomstype;
	}
	public void setRoomstype(String roomstype) {
		this.roomstype = roomstype;
	}
	public double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	
	
}
