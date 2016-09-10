package com.digitalBorderlands.cityHall.data.comm;

/**
 * Internal class, exposed for the purposes of testing.
 * 
 * @author Alex
 */
public interface ClientConfig {
	String getApiUrl();
	void setApiUrl(String apiUrl);
	
	String getUsername();
	void setUsername(String username);
	
	String getPasshash();
	void setPassword(String password);
}
