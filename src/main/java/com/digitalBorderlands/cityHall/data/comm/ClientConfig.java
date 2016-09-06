package com.digitalBorderlands.cityHall.data.comm;

public interface ClientConfig {
	String getApiUrl();
	void setApiUrl(String apiUrl);
	
	String getUsername();
	void setUsername(String username);
	
	String getPasshash();
	void setPassword(String password);
}
