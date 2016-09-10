package com.digitalBorderlands.cityHall;

import com.digitalBorderlands.cityHall.data.comm.ClientConfig;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.impl.Container;

/**
 * Entry point into the library, used to instantiate a settings class.
 * 
 * @author Alex
 *
 */
public abstract class Settings {
	
	/**
	 * Create a Settings class, using the values in resources.cityhall/server.properties
	 * If no user name is specified in the properties file, it will use the current machine name.
	 * If no password is specified in the properties file, it will use a blank password.
	 * 
	 * @return	        Settings object which can be used to access City Hall
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public static Settings create() throws CityHallException {
		return Settings.create(null, null, null);
	}
	
	/**
	 * Create a Settings class, using the values in resources.cityhall/server.properties
	 * If no user name is specified in the properties file, it will use the current machine name.
	 * If no password is specified in the properties file, it will use a blank password.
	 * 
	 * @param url       URI to override the properties file with. (e.x. "http://cityhall:8080/api/")
	 * @return	        Settings object which can be used to access City Hall
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public static Settings create(String url) throws CityHallException {
		return Settings.create(url, null, null);
	}
	
	/**
	 * Create a Settings class, using the values in resources.cityhall/server.properties
	 * If no user name is specified in the properties file, it will use the current machine name.
	 * If no password is specified in the properties file, it will use a blank password.
	 * 
	 * @param url       URI to override the properties file with. (e.x. "http://cityhall:8080/api/")
	 * @param username  Login name to override the properties file with.
	 * @return	        Settings object which can be used to access City Hall
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public static Settings create(String url, String username) throws CityHallException {
		return Settings.create(url, username, null);
	}
	
	/**
	 * Create a Settings class, using the values in resources.cityhall/server.properties
	 * If no user name is specified in the properties file, it will use the current machine name.
	 * If no password is specified in the properties file, it will use a blank password.
	 * 
	 * @param url       URI to override the properties file with. (e.x. "http://cityhall:8080/api/")
	 * @param username  Login name to override the properties file with.
	 * @param password  Plain-text password to override the properties file with. 
	 * @return	        Settings object which can be used to access City Hall
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public static Settings create(String url, String username, String password) throws CityHallException {
		Settings ret = Container.settings.get();
		ret.open(Container.clientConfig.get(url, username, password));
		return ret;
	}
	
	protected Settings(Environments environments, Users users, Values values) {
		this.environments = environments;
		this.users = users;
		this.values = values;
	}
	
	/**
	 * This is done for namespacing convenience, all functionality related 
	 * to environments can be accessed through this variable.
	 */
	public final Environments environments;
	
	/**
	 * This is done for namespacing convenience, all functionality related 
	 * to users can be accessed through this variable.
	 */
	public final Users users;
	
	/**
	 * This is done for namespacing convenience, all functionality related 
	 * to values (including history and children) can be accessed through this variable.
	 */
	public final Values values;
	
	/**
	 * @return the current logged in user.
	 */
	public abstract String getUser();
	
	/**
	 * Log out of City Hall. Any subsequent call will throw a NotLoggedInException
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public abstract void logout() throws CityHallException;
	
	/**
	 * @return true if logged in, false otherwise. If this function returns false,
	 * any subsequent call to a function that `throws CityHallException` will 
	 * throw a NotLoggedInException
	 */
	public abstract Boolean isLoggedIn();
	
	/**
	 * Use this to update the password of the current logged in user. 
	 * 
	 * @param password   Plain-text password to update to
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public abstract void updatePassword(String password) throws CityHallException;
	
	/**
	 * This function is here since it's expected to be the most used. 
	 * All other gets should route through values property. This function
	 * will use the default environment and the most appropriate override.
	 * 
	 * @param path    The path to the value to be retrieved.
	 * @return the value from the server, or null if the current logged in user doesn't have access or the value doesn't exist
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public String get(String path) throws CityHallException {
		return this.values.get(path);
	}
	
	protected abstract void open(ClientConfig config) throws CityHallException;
}
