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

public class Settings {
	
	private enum Status {
		NotLoggedIn,
		LoggedIn,
		LoggedOut
	}
	
	public Settings() throws CityHallException {
		this(Settings.GetHostname(), "", Container.Self.getComponent(Client.class)); 
	}
	
	public Settings(String user, String password, Client client) throws CityHallException {
		this.user = user;
		this.client = client;
		
		this.login(password);
		this.getDefaultEnvironment();
	}

	private Status loggedIn = Status.NotLoggedIn;
	private final String user;
	private String defaultEnvironment = "";
	private final Client client;
	private final Lock lock = new ReentrantLock();
	
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
	
	private void getDefaultEnvironment() throws CityHallException {
		if (this.loggedIn == Status.LoggedIn) {
			String location = String.format("auth/user/%s/default/", this.user);
			ValueResponse dev = this.client.get(location, ValueResponse.class);
			this.defaultEnvironment = dev.Value;
		}
	}
	
	public String getUser()	{
		return this.user;
	}
	
	public String getDefaultEnviornment() {
		return this.defaultEnvironment;
	}
	
	public Boolean isLoggedIn() {
		return this.loggedIn == Status.LoggedIn;
	}	
}
