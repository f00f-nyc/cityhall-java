package com.digitalBorderlands.cityHall.impl;

import com.digitalBorderlands.cityHall.Settings;
import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.comm.ClientConfig;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;

public class Container {

	// Will be used for testing, a one-time override of the Client
	// implementation to allow for mocking it with a different one.
	// This has to be done because the IoC container tried (PicoContainer)
	// seemed to have trouble instantiating internal classes, specifically
	// com.digitalBorderlands.cityhall.impl.*Impl classes.
	private static Client override;
	
	public static ClientConfig getClientConfig(String url, String username, String password) {
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
	
	static void setClient(Client override) {
		Container.override = override;		
	}
	
	public static Client getClient() {
		if (override != null) {
			Client ret = Container.override;
			Container.override = null;
			return ret;
		}
		
		return new ClientImpl();
	}
	
	public static Client getOpenClient(String url, String username, String password) throws CityHallException {
		ClientConfig config = Container.getClientConfig(url, username, password);
		Client client = Container.getClient();
		client.open(config);
		return client;
	}

	public static Settings getSettings() throws CityHallException {
		return new SettingsImpl();
	}
}
