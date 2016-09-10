package com.digitalBorderlands.cityHall.data;

import java.time.Instant;

/**
 * A specific change to a value
 * @author Alex
 *
 */
public class LogEntry {
	/**
	 * The id of that value in the database
	 */
    public int id;
    
    /**
     * The name of the value, the last part of the path
     */
    public String name;
    
    /**
     * The actual value at this point
     */
    public String value;
    
    /**
     * The user name of the author who created this LogEntry
     */
    public String author;
    
    /**
     * The time stamp when this change was made
     */
    public Instant datetime;
    
    /**
     * true, if this is the last entry.  false, otherwise.
     */
    public Boolean active;
    
    /**
     * The actual protect bit at this point
     */
    public Boolean protect;
    
    /**
     * The name of the override, if "" then default override
     */
    public String override;
}
