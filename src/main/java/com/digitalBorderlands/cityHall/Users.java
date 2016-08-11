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

public abstract class Users {
	protected Users(Settings parent) {
		this.parent = parent;
	}
	
	private Settings parent;
	
	public UserInfo getUser(String user) throws CityHallException {
		String location = String.format("/auth/user/%s/", user);
		UserResponse resp = this.parent.get(location, null, UserResponse.class);
		UserInfo ret = new UserInfo();
		List<UserRights> list = new ArrayList<UserRights>();
		for (Entry<String, Integer> entry: resp.Environments.entrySet()) {
			list.add(new UserRights(entry.getKey(), entry.getValue()));
		}
		ret.rights = list.toArray(new UserRights[list.size()]);
		return ret;
	}
	
	public void createUser(String user, String password) throws CityHallException {
		if (user.equals(this.parent.getUser())) {
			throw new InvalidRequestException("You are passing your own user name to CreateUser(). Please use UpdatePassword() to update your own password", null);
		}
		
		String location = String.format("auth/user/%s/", user);
		HashMap<String,String> body = new HashMap<String, String>();
		body.put("passhash", Password.hash(password));
		this.parent.post(location, body, BaseResponse.class);
	}
	
	public void deleteUser(String user) throws CityHallException {
		String location = String.format("auth/user/%s/", user);
		this.parent.delete(location, BaseResponse.class);
	}
	
	public void grant(String user, String environment, Rights rights) throws CityHallException {
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("env", environment);
		body.put("user", user);
		body.put("rights", Integer.toString(rights.intValue()));
		
		this.parent.post("auth/grant/", body,  BaseResponse.class);
	}
}
