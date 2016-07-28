package com.digitalBorderlands.cityHall.data.responses;

import java.util.Map;

public class EnvironmentResponse extends BaseResponse {
	public Map<String, Integer> Users;

	public void put(String key, int value) {
		this.Users.put(key, new Integer(value));		
	}
}
