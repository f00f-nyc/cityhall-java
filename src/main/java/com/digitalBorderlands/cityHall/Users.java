package com.digitalBorderlands.cityHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.digitalBorderlands.cityHall.data.Rights;
import com.digitalBorderlands.cityHall.data.UserInfo;
import com.digitalBorderlands.cityHall.data.UserRights;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.UserResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.exceptions.InvalidRequestException;
import com.digitalBorderlands.cityHall.impl.Password;
import com.digitalBorderlands.cityHall.impl.SettingsClient;

/**
 * This class is for accessing all User-related functionality
 * 
 * @author Alex
 *
 */
public abstract class Users {
	protected SettingsClient parent;
	
	/**
	 * Return information about a given user.
	 * 
	 * @param user The user to retrieve
	 * @return A UserInfo class with information about the user's permsissions.
	 * @throws CityHallException
	 */
	public UserInfo getUser(String user) throws CityHallException {
		String location = String.format("/auth/user/%s/", user);
		UserResponse resp = this.parent.get(location, null, UserResponse.class);
		UserInfo ret = new UserInfo();
		List<UserRights> list = new ArrayList<UserRights>();
		for (Entry<String, Integer> entry: resp.environments.entrySet()) {
			list.add(new UserRights(entry.getKey(), entry.getValue()));
		}
		ret.rights = list.toArray(new UserRights[list.size()]);
		return ret;
	}
	
	/**
	 * Create a user
	 * 
	 * @param user      The user name to create.  Will throw ErrorFromCityHallException if the user already exists
	 * @param password  The plain text password for the user.
	 * @throws CityHallException
	 */
	public void createUser(String user, String password) throws CityHallException {
		if (user.equals(this.parent.getUser())) {
			throw new InvalidRequestException("You are passing your own user name to CreateUser(). Please use UpdatePassword() to update your own password", null);
		}
		
		String location = String.format("auth/user/%s/", user);
		HashMap<String,String> body = new HashMap<String, String>();
		body.put("passhash", Password.hash(password));
		this.parent.post(location, body, null, BaseResponse.class);
	}
	
	/**
	 * Delete a user
	 * 
	 * @param user  The user name to delete.  Must have grant access to all of his environments, or to users environment.
	 * 
	 * @throws CityHallException
	 */
	public void deleteUser(String user) throws CityHallException {
		String location = String.format("auth/user/%s/", user);
		this.parent.delete(location, BaseResponse.class);
	}
	
	/**
	 * Grant permissions `rights` to a user `user` for environment `environment`.
	 * 
	 * @param user          the user to grant permissions to
	 * @param environment   the environment to grant permissions on (logged in user must have Grant permissions on it)
	 * @param rights        the rights to grant to `user`
	 * @throws CityHallException
	 */
	public void grant(String user, String environment, Rights rights) throws CityHallException {
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("env", environment);
		body.put("user", user);
		body.put("rights", Integer.toString(rights.intValue()));
		
		this.parent.post("auth/grant/", body, null, BaseResponse.class);
	}
}
