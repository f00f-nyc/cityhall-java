package com.digitalBorderlands.cityHall.exceptions;

/**
 * Attempted an operation while not logged in.
 * 
 * @author Alex
 *
 */
public class NotLoggedInException extends CityHallException {
	private static final long serialVersionUID = 1L;

	public NotLoggedInException() {
		super("Client has logged off");
	}
}
