package com.digitalBorderlands.cityHall.data.responses;

import java.util.HashMap;

public class UserResponse extends BaseResponse {
	public HashMap<String, Integer> Environments;

	public void put(String key, int value) {
		this.Environments.put(key, new Integer(value));		
	}
}
