package com.digitalBorderlands.cityHall.data.responses;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

/**
 * Internal class, exposed for the purposes of testing.
 * 
 * @author Alex
 */
public class UserResponse extends BaseResponse {
	@SerializedName("Environments")
	public HashMap<String, Integer> environments;

	public void put(String key, int value) {
		this.environments.put(key, new Integer(value));		
	}
}
