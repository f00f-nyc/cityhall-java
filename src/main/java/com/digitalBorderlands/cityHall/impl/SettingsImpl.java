package com.digitalBorderlands.cityHall.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.digitalBorderlands.cityHall.Environments;
import com.digitalBorderlands.cityHall.Settings;
import com.digitalBorderlands.cityHall.Users;
import com.digitalBorderlands.cityHall.Values;
import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.comm.ClientConfig;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.exceptions.NotLoggedInException;

class SettingsImpl extends Settings implements SettingsClient {

	private enum Status {
		NotLoggedIn,
		LoggedIn,
		LoggedOut
	}
	
	private static class EnviromentsImpl extends Environments { 
		void setSettingsImpl(SettingsImpl parent) {
			this.parent = parent;
		}
		
		void loginDefaultEnvironment(String defaultEnvironment) {
			this.defaultEnvironment = defaultEnvironment;
		}
	}
	
	private static class UsersImpl extends Users {
		void setSettingsImpl(SettingsImpl parent) {
			this.parent = parent;
		}
	}
	
	private static class ValuesImpl extends Values {
		void setSettingsImpl(SettingsImpl parent) {
			this.parent = parent;
		}
	}
	
	public <T extends BaseResponse> T post(String location, Map<String,String> body, Map<String, String> queryParams, Class<T> type) throws CityHallException {
		this.ensureLoggedIn();
		return this.client.post(location, body, queryParams, type);
	}
	
	public <T extends BaseResponse> T get(String location, Map<String, String> queryParams, Class<T> type) throws CityHallException {
		this.ensureLoggedIn();
		return this.client.get(location, queryParams, type);
	}
	
	public <T extends BaseResponse> T delete(String location, Class<T> type) throws CityHallException {
		this.ensureLoggedIn();
		return this.client.delete(location, type);
	}
	
	public <T extends BaseResponse> T put(String location, Map<String,String> body, Class<T> type) throws CityHallException {
		this.ensureLoggedIn();
		return this.client.put(location, body, type);
	}

	public void close() throws CityHallException {
		this.logout();
	}
	
	private void ensureLoggedIn() throws CityHallException {
    	if (!this.isLoggedIn()) {
    		throw new NotLoggedInException();
    	}
    }
	
	public SettingsImpl() {
		super(new EnviromentsImpl(), new UsersImpl(), new ValuesImpl());
	}
	
	@Override
	public void open(ClientConfig config) throws CityHallException {
		this.user = config.getUsername();
		
		if ((user == null) || user.equalsIgnoreCase("")) {
			throw new NotLoggedInException();
		}
		
		this.client.open(config);
		
		this.login(config.getPasshash());
		String defaultEnvironment = this.getDefaultEnvironment();
		
		EnviromentsImpl envImpl = (EnviromentsImpl)this.environments;
		envImpl.setSettingsImpl(this);
		envImpl.loginDefaultEnvironment(defaultEnvironment);
		
		UsersImpl userImpl = (UsersImpl)this.users;
		userImpl.setSettingsImpl(this);
		
		ValuesImpl valuesImpl = (ValuesImpl)this.values;
		valuesImpl.setSettingsImpl(this);
	}
	
	private Status loggedIn = Status.NotLoggedIn;
	private final Client client = Container.client.get();
	private final Lock lock = new ReentrantLock();
	private String user;
		
	private void login(String passhash) throws CityHallException {
		try {
			this.lock.lock(); 
			
			if (this.loggedIn == Status.NotLoggedIn) {
				HashMap<String, String> body = new HashMap<String, String>();
				body.put("username", this.user);
				body.put("passhash", passhash);
				this.client.post("auth/", body, null, BaseResponse.class);
				this.loggedIn = Status.LoggedIn;
			}
		} finally {
			this.lock.unlock();
		}
	}
	
	private String getDefaultEnvironment() throws CityHallException {
		String location = String.format("auth/user/%s/default/", this.user);
		ValueResponse dev = this.client.get(location, null, ValueResponse.class);
		return dev.value;
	}
	
	public String getCachedDefaultEnviornment() {
		return this.environments.getDefaultEnviornment();
	}

	@Override
	public void logout() throws CityHallException {
		try {
			this.lock.lock();
			
			if (this.loggedIn == Status.LoggedIn) {
				this.client.delete("auth/", BaseResponse.class);
				this.loggedIn = Status.LoggedOut;
				this.client.close();
			}
		}
		finally {
			this.lock.unlock();
		}
	}
	
	@Override
	public String getUser() {
		return this.user;
	}
	
	@Override
	public Boolean isLoggedIn() {
		return this.loggedIn == Status.LoggedIn;
	}
	
	@Override
	public void updatePassword(String password) throws CityHallException {
		String location = String.format("auth/user/%s/", this.user);
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("passhash", Password.hash(password));
		this.put(location, body, BaseResponse.class);		
	}
}
