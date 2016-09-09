package com.digitalBorderlands.cityHall.impl;

import com.digitalBorderlands.cityHall.Settings;
import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.comm.ClientConfig;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;

public class Container {
	
	public static class client {
		public static Client get() {
			return new ClientImpl();
		}
	}
	
	public static class clientConfig {
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
	
	public static class settings {
		public static Settings get() throws CityHallException {
			return new SettingsImpl();
		}	
	}
	
	public static Client getOpenClient(String url, String username, String password) throws CityHallException {
		ClientConfig config = Container.clientConfig.get(url, username, password);
		Client client = Container.client.get();
		client.open(config);
		return client;
	}
}
