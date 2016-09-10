package com.digitalBorderlands.cityHall.exceptions;

/**
 * City Hall was reached, but an error was retrieved as response.
 * 
 * @author Alex
 *
 */
public class ErrorFromCityHallException extends CityHallException {
	private static final long serialVersionUID = 1L;
	
	public ErrorFromCityHallException(String message) {
		super(message);
	}
}
