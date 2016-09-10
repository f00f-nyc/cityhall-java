package com.digitalBorderlands.cityHall.data;

/**
 * A Child object
 * 
 * @author Alex
 *
 */
public class Child {
	/**
	 * The id of the value in the database
	 */
    public int id;
    
    /**
     * The name of the value, the last part of the path 
     */
    public String name;
    
    /**
     * The override of the value, or "" if default
     */
    public String override;
    
    /**
     * The full path within the environment
     */
    public String path;
    
    /**
     * The current value.
     */
    public String value;
    
    
    /**
     * The current protect bit
     */
    public Boolean protect;
}
