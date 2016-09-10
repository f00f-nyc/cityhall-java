package com.digitalBorderlands.cityHall.data.responses;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * Internal class, exposed for the purposes of testing.
 * 
 * @author Alex
 */
public class EnvironmentResponse extends BaseResponse {
	@SerializedName("Users")
	public Map<String, Integer> users;

	public void put(String key, int value) {
		this.users.put(key, new Integer(value));		
	}
}
