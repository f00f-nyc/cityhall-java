package com.digitalBorderlands.cityHall.exceptions;

public class InvalidRequestException extends CityHallException {
	private static final long serialVersionUID = 1L;
	
	public InvalidRequestException(String url, int statusCode) {
		super(InvalidRequestException.ErrorMessageFromStatusCode(url, statusCode));
		this.inner = null;
	}
	
	public InvalidRequestException(String message, Exception inner) {
		super(message);
		this.inner = inner;
	}
	
	private Exception inner;
	public Exception getInnerException() {
		return this.inner;
	}
	
	private static String ErrorMessageFromStatusCode(String url, int statusCode) {
		return String.format("Attempted to reach %s but got back status code %d", url, statusCode);
	}
}
