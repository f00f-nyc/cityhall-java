package com.digitalBorderlands.cityHall.data;

/**
 * A complete value from the database
 * 
 * @author Alex
 *
 */
public class Value {
	/**
	 * The current value, may be null.
	 */
	public String value;
	
	/**
	 * The current protect bit, may be null
	 */
	public Boolean protect;
	
	/**
	 * Default constructor
	 */
	public Value() { }
	
	/**
	 * @param value     the value, may be null
	 * @param protect   the protect bit, may be null
	 */
	public Value(String value, Boolean protect) {
		this.value = value;
		this.protect = protect;
	}
}
