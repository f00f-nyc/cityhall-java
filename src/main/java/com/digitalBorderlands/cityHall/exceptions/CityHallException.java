package com.digitalBorderlands.cityHall.exceptions;

/**
 * Base exception class for errors thrown by the City Hall library
 * 
 * @author Alex
 *
 */
public abstract class CityHallException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public CityHallException(String message) {
		super(message);
	}
}
