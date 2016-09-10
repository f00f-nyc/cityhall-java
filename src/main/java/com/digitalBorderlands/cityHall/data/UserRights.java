package com.digitalBorderlands.cityHall.data;

/**
 * A user and rights tuple.
 * 
 * @author Alex
 *
 */
public class UserRights {
	
	/**
	 * The environment
	 */
	public String environment;
	
	/**
	 * The users rights on that environment
	 */
	public Rights rights;
	
	/**
	 * Default constructor
	 */
	public UserRights() { }
	
	/**
	 * Helpful constructor to build from enumeration
	 * 
	 * @param environment   the environment
	 * @param rights	    the user's rights on that environment
	 */
	public UserRights(String environment, Integer rights) {
		this.environment = environment;
		this.rights = com.digitalBorderlands.cityHall.data.Rights.ALL.get(rights);
	}
}
