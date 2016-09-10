package com.digitalBorderlands.cityHall.data;

/**
 * A user and rights tuple for a given environment
 * 
 * @author Alex
 *
 */
public class EnvironmentRights {
	/**
	 * The user
	 */
	public String user;
	
	/**
	 * That user's rights
	 */
	public Rights rights;
	
	/**
	 * default constructor
	 */
	public EnvironmentRights() { }
	
	/**
	 * Helpful constructor
	 * 
	 * @param user    the user
	 * @param rights  that user's rights as an Integer
	 */
	public EnvironmentRights(String user, Integer rights) {
		this.user = user;
		this.rights = com.digitalBorderlands.cityHall.data.Rights.ALL.get(rights);
	}
}
