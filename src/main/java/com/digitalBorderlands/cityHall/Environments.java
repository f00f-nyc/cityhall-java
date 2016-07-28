package com.digitalBorderlands.cityHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.digitalBorderlands.cityHall.data.EnvironmentInfo;
import com.digitalBorderlands.cityHall.data.EnvironmentRights;
import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.EnvironmentResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;

public abstract class Environments {
	protected Environments(Client client, Settings parent, String defaultEnvironment) {
		this.client = client;
		this.parent = parent;
		this.defaultEnvironment = defaultEnvironment;
	}
	
	protected final Client client;
	protected final Settings parent;
	protected String defaultEnvironment;
	
	public String getDefaultEnviornment() {
		return this.defaultEnvironment;
	}
	
	public void setDefaultEnvironment(String defaultEnvironment) throws CityHallException {
		this.parent.ensureLoggedIn();
		String location = String.format("auth/user/%s/default/", this.parent.getUser());
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("env", defaultEnvironment);
		this.client.post(location, body, BaseResponse.class);
		this.defaultEnvironment = defaultEnvironment;
	}
	
	public EnvironmentInfo get(String environmentName) throws CityHallException {
		this.parent.ensureLoggedIn();
		String location = String.format("auth/env/%s/", environmentName);
		EnvironmentResponse res = this.client.get(location, EnvironmentResponse.class);
		EnvironmentInfo ret = new EnvironmentInfo();
		List<EnvironmentRights> list = new ArrayList<EnvironmentRights>(); 
		for(Entry<String,Integer> entry : res.Users.entrySet()) {
			list.add(new EnvironmentRights(entry.getKey(), entry.getValue()));
		}
		ret.Rights = list.toArray(new EnvironmentRights[list.size()]);
		return ret;
	}
	
	public void create(String environmentName) throws CityHallException {
		this.parent.ensureLoggedIn();
		String location = String.format("auth/env/%s/", environmentName);
		this.client.post(location, null, BaseResponse.class);
	}
}
