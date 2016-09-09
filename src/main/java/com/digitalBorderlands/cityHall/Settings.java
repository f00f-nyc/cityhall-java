package com.digitalBorderlands.cityHall;

import com.digitalBorderlands.cityHall.data.comm.ClientConfig;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.impl.Container;

public abstract class Settings {
	public static Settings create() throws CityHallException {
		Settings ret = Container.settings.get();
		ret.open(Container.clientConfig.get(null, null, null));
		return ret;
	}
	
	public static Settings create(String url) throws CityHallException {
		Settings ret = Container.settings.get();
		ret.open(Container.clientConfig.get(url, null, null));
		return ret;
	}
	
	public static Settings create(String url, String username) throws CityHallException {
		Settings ret = Container.settings.get();
		ret.open(Container.clientConfig.get(url, username, null));
		return ret;
	}
	
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
	
	public final Environments environments;
	public final Users users;
	public final Values values;
	
	public abstract String getUser();
	public abstract void logout() throws CityHallException;
	public abstract Boolean isLoggedIn();
	public abstract void updatePassword(String password) throws CityHallException;
	
	/*
	 * This function is here since it's expected to be the most used. 
	 * All other gets should route through values property
	 */
	public String get(String path) throws CityHallException {
		return this.values.get(path);
	}
	
	protected abstract void open(ClientConfig config) throws CityHallException;
}
