package com.digitalBorderlands.cityHall.data.comm;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import com.digitalBorderlands.cityHall.Password;

public class ClientConfigImpl implements ClientConfig {
	
	public static ClientConfig New() {
		return new ClientConfigImpl();
	}
	
	public static ClientConfig New(String url) {
		ClientConfig ret = ClientConfigImpl.New();
		ret.setApiUrl(url);
		return ret;
	}
	
	public static ClientConfig New(String url, String username) {
		ClientConfig ret = ClientConfigImpl.New(url);
		ret.setUsername(username);
		return ret;
	}
	
	public static ClientConfig New(String url, String username, String password) {
		ClientConfig ret = ClientConfigImpl.New(url, username);
		ret.setPassword(password);
		return ret;
	}
	
	public ClientConfigImpl() {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = ClientConfigImpl.class.getClassLoader().getResourceAsStream("resources/cityhall/server.properties");

			if (input != null) {
				prop.load(input);
			
				this.apiUrl = ClientConfigImpl.getApiUrl(prop);
				this.username = ClientConfigImpl.getUsername(prop);
				this.passhash = Password.hash(ClientConfigImpl.getPassword(prop));
			}
		} catch (IOException ex) {
			// suppress exception
		} finally {
			try {
				if (input != null) {
						input.close();
				}
			} catch (IOException ex) {
				// suppress exception
			}
		}		
	}
	
	private static String getApiUrl(Properties prop) {
		String url = prop.getProperty("url");
		return (url == null) ? "" : url;
	}

	private static String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "guest";
		}
	}
	
	private static String getUsername(Properties prop) {
		String username = prop.getProperty("username");
		return (username == null) ? ClientConfigImpl.getHostname() : username;
	}
	
	private static String getPassword(Properties prop) {		
		String password = prop.getProperty("password");
		return (password == null) ? "" : password;
	}
	
	private String apiUrl;
	private String username;
	private String passhash;
	
	@Override
	public String getApiUrl() {
		return this.apiUrl;
	}
	
	@Override
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getPasshash() {
		return this.passhash;
	}
	
	@Override
	public void setPassword(String password) {
		this.passhash = Password.hash(password);
	}
}
