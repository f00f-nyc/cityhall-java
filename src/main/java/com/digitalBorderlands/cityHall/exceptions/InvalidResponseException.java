package com.digitalBorderlands.cityHall.exceptions;

/**
 * City Hall has returned an unexpected, invalid response.  For example, a 500 Internal Server Error.
 * 
 * @author Alex
 *
 */
public class InvalidResponseException extends CityHallException {
	private static final long serialVersionUID = 1L;

	public InvalidResponseException(String message) {
		super(message);
	}
}
