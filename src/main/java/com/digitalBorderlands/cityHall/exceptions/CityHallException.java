package com.digitalBorderlands.cityHall.exceptions;

public abstract class CityHallException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public CityHallException(String message) {
		super(message);
	}
}
