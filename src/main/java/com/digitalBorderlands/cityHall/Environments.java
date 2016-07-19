package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
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
}
