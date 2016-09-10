package com.digitalBorderlands.cityHall.impl;

import com.digitalBorderlands.cityHall.Settings;
import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.comm.ClientConfig;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;

/**
 * Internal class, exposed for the purposes of testing.
 * Note that this is done this way as a stop gap measure. The IoC Container
 * had trouble instantiating the non-public *Impl classes. 
 * 
 * @author Alex
 */
public class Container {
	
	/**
	 * Internal class, exposed for the purposes of testing.
	 * This is exposed as a static class with a single static method so that it
	 * can be PowerMock'd.
	 * 
	 * @author Alex
	 *
	 */
	public static class client {
		/**
		 * @return A new Client instance, in un-opened state.
		 */
		public static Client get() {
			return new ClientImpl();
		}
	}
	
	/**
	 * Internal class, exposed for the purposes of testing.
	 * This is exposed as a static class with a single static method so that it
	 * can be PowerMock'd.
	 * 
	 * @author Alex
	 *
	 */
	public static class clientConfig {
		/**
		 * Return instance of ClientConfig with values from resources.cityhall/server.properties
		 * 
		 * @param url        the url to use, if null, use value from resources.cityhall/server.properties
		 * @param username   the user name to use, if null, use value from resources.cityhall/server.properties
		 * @param password   the password to use, if null, use value from resources.cityhall/server.properties
		 * @return ClientConfig instance with the given settings
		 */
		public static ClientConfig get(String url, String username, String password) {
			ClientConfig config = new ClientConfigImpl();
			
			if (url != null) {
				config.setApiUrl(url);
			}
			
			if (username != null) {
				config.setUsername(username);
			}
			
			if (password != null) {
				config.setPassword(password);
			}
			
			return config;
		}		
	}
	
	/**
	 * Internal class, exposed for the purposes of testing.
	 * This is exposed as a static class with a single static method so that it
	 * can be PowerMock'd.
	 * 
	 * @author Alex
	 *
	 */
	public static class settings {
		/**
		 * Equivalent to calling com.digitalBorderlands.cityHall.Settings.create()
		 * 
		 * @return new Settings object, with properties from resources.cityhall/server.properties
		 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
		 */
		public static Settings get() throws CityHallException {
			return new SettingsImpl();
		}	
	}

	/**
	 * Create a new Client instance, and call open() on it with the given configuration. 
	 * If null is specified for 
	 * 
	 * @param url			The url to use, if null, use value from resources.cityhall/server.properties
	 * @param username		The user name to use, if null, use value from resources.cityhall/server.properties
	 * @param password      The password to use, if null, use value from resources.cityhall/server.properties
	 * @return Client instance, with open() already called on it.
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public static Client getOpenClient(String url, String username, String password) throws CityHallException {
		ClientConfig config = Container.clientConfig.get(url, username, password);
		Client client = Container.client.get();
		client.open(config);
		return client;
	}
}
