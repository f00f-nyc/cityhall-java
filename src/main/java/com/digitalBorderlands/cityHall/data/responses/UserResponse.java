package com.digitalBorderlands.cityHall.data.responses;

import java.util.Map;

public class UserResponse extends BaseResponse {
	public Map<String, Integer> Environments;

	public void put(String key, int value) {
		this.Environments.put(key, new Integer(value));		
	}
}
