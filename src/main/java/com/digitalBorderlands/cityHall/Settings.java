package com.digitalBorderlands.cityHall;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.digitalBorderlands.cityHall.config.Container;
import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.exceptions.NotLoggedInException;

public class Settings {

	private enum Status {
		NotLoggedIn,
		LoggedIn,
		LoggedOut
	}
	
	private static class EnvInstance extends Environments { 
		public EnvInstance(Client client, Settings parent, String defaultEnvironment) {
			super(client, parent, defaultEnvironment);
		}
	}

	public Settings() throws CityHallException {
		this(Settings.GetHostname(), "", Container.Self.getComponent(Client.class)); 
	}
	
	public Settings(String user, String password, Client client) throws CityHallException {
		this.user = user;
		this.client = client;
		
		this.login(password);
		String defaultEnvironment = this.getDefaultEnvironment();
		
		this.environments = new EnvInstance(this.client, this, defaultEnvironment);
	}

	private Status loggedIn = Status.NotLoggedIn;
	private final String user;
	private final Client client;
	private final Lock lock = new ReentrantLock();
	
	public final Environments environments;
	
	private static String GetHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "guest";
		}
	}
		
	private void login(String password) throws CityHallException {
		try {
			this.lock.lock(); 
			
			if (this.loggedIn == Status.NotLoggedIn) {
				HashMap<String, String> body = new HashMap<String, String>();
				body.put("username", this.user);
				body.put("passhash", Password.hash(password));
				this.client.post("auth/", body, BaseResponse.class);
				this.loggedIn = Status.LoggedIn;
			}
		} finally {
			this.lock.unlock();
		}
	}
	
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
	
	private String getDefaultEnvironment() throws CityHallException {
		String location = String.format("auth/user/%s/default/", this.user);
		ValueResponse dev = this.client.get(location, ValueResponse.class);
		return dev.Value;
	}
	
	public String getUser()	{
		return this.user;
	}
	
	public Boolean isLoggedIn() {
		return this.loggedIn == Status.LoggedIn;
	}

    void ensureLoggedIn() throws CityHallException {
    	if (!this.isLoggedIn()) {
    		throw new NotLoggedInException();
    	}
    }
}
