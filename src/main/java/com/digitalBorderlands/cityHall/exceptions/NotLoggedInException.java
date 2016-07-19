package com.digitalBorderlands.cityHall.exceptions;

public class NotLoggedInException extends CityHallException {
	private static final long serialVersionUID = 1L;

	public NotLoggedInException() {
		super("Client has logged off");
	}
}
