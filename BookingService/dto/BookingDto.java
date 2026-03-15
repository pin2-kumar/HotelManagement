package com.BookingService.dto;

import java.time.LocalDate;
import java.util.List;

public class BookingDto {

	
	private long PropertyId;
	private long roomId;
	private long roomAvailabilityId;
	private String name;
	private String email;
	private String mobile;
	private String status;
	private double price;
	private int totalNights;
	private double totalPrice;
	private List<LocalDate> date;
	
	
	
	
	
	public long getPropertyId() {
		return PropertyId;
	}
	public void setPropertyId(long propertyId) {
		PropertyId = propertyId;
	}
	public long getRoomId() {
		return roomId;
	}
	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}
	public long getRoomAvailabilityId() {
		return roomAvailabilityId;
	}
	public void setRoomAvailabilityId(long roomAvailabilityId) {
		this.roomAvailabilityId = roomAvailabilityId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getTotalNights() {
		return totalNights;
	}
	public void setTotalNights(int totalNights) {
		this.totalNights = totalNights;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<LocalDate> getDate() {
		return date;
	}
	public void setDate(List<LocalDate> date) {
		this.date = date;
	}
	
	
}
