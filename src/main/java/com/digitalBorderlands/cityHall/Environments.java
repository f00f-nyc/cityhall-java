package com.digitalBorderlands.cityHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.digitalBorderlands.cityHall.data.EnvironmentInfo;
import com.digitalBorderlands.cityHall.data.EnvironmentRights;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.EnvironmentResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.impl.SettingsClient;

/**
 * All environments-related functionality
 * 
 * @author Alex
 *
 */
public abstract class Environments {
	protected SettingsClient parent;
	protected String defaultEnvironment;
	
	/**
	 * The default environment for the current logged in user
	 * @return the default environment.  If null, then no default environment has been set.
	 */
	public String getDefaultEnviornment() {
		return this.defaultEnvironment;
	}
	
	/**
	 * Set a new default environment for the current logged in user.
	 * @param defaultEnvironment the new default environment
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public void setDefaultEnvironment(String defaultEnvironment) throws CityHallException {
		String location = String.format("auth/user/%s/default/", this.parent.getUser());
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("env", defaultEnvironment);
		this.parent.post(location, body, null, BaseResponse.class);
		this.defaultEnvironment = defaultEnvironment;
	}
	
	/**
	 * Retrieve Environmental info (user permissions) for a given environment.
	 * @param environmentName the name of the environment to query
	 * @return the given environment info. 
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public EnvironmentInfo get(String environmentName) throws CityHallException {
		String location = String.format("auth/env/%s/", environmentName);
		EnvironmentResponse res = this.parent.get(location, null, EnvironmentResponse.class);
		EnvironmentInfo ret = new EnvironmentInfo();
		List<EnvironmentRights> list = new ArrayList<EnvironmentRights>(); 
		for(Entry<String,Integer> entry : res.users.entrySet()) {
			list.add(new EnvironmentRights(entry.getKey(), entry.getValue()));
		}
		ret.rights = list.toArray(new EnvironmentRights[list.size()]);
		return ret;
	}
	
	/**
	 * Create an environment. Since environments, like users, are lightweight objects any user is free to create environments.
	 * @param environmentName the new environment. The current logged in user will be automatically granted Grant permissions on it
	 * @throws CityHallException refer to com.digitalBorderlands.cityhall.exceptions for exceptions
	 */
	public void create(String environmentName) throws CityHallException {
		String location = String.format("auth/env/%s/", environmentName);
		this.parent.post(location, null, null, BaseResponse.class);
	}
}
