package com.digitalBorderlands.cityHall.data.comm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;

import com.digitalBorderlands.cityHall.data.responses.BaseResponse;

public class Expected {
	public final BaseResponse response;
	public final String method;
	public final String location;
	public final HashMap<String, String> body;
	public final HashMap<String, String> queryParams;
	
	public Expected() {
		this(Responses.ok(), null, null, null);
	}
	
	public Expected(BaseResponse response, String method) {
		this(response, method, null, null);
	}
	
	public Expected(BaseResponse response, String method, String location, HashMap<String, String> body) {
		this.response = response;
		this.method = method;
		this.location = location;
		this.body = body;
		this.queryParams = null;
	}
	
	public Expected(BaseResponse response, String location, HashMap<String, String> queryParams) {
		this.response = response;
		this.method = "GET";
		this.location = location;
		this.body = null;
		this.queryParams = queryParams;
	}
	
	public Expected(BaseResponse response, String method, String location, HashMap<String, String> body, HashMap<String, String> queryParams) {
		this.response = response;
		this.method = method;
		this.location = location;
		this.body = body;
		this.queryParams = queryParams;
	}
	
	private void checkMethod(String method) {
		if (this.method != null) {
			Assert.assertEquals(method.toUpperCase(), this.method.toUpperCase());
		}
	}
	
	private void checkLocation(String location) {
		if (this.location != null) {
			Assert.assertEquals(location, this.location);
		}
	}
	
	private void checkMap(Map<String, String> expected, Map<String, String> actual) {
		if ((expected != null) && (actual != null)) {
			Assert.assertEquals(expected.size(), actual.size());
			
			for(Entry<String, String> entry : expected.entrySet()) {
				Assert.assertTrue(actual.containsKey(entry.getKey()));
				Assert.assertEquals(entry.getValue(), actual.get(entry.getKey()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseResponse> T checkType(Class<T> type) {
		Assert.assertEquals(type, this.response.getClass());
		return (T)this.response;
	}
	
	public <T extends BaseResponse> T checkPost(String location, Map<String, String> body, Map<String, String> queryParams, Class<T> type) {
		this.checkMethod("POST");
		this.checkLocation(location);
		this.checkMap(this.body, body);
		this.checkMap(this.queryParams, queryParams);
		return this.checkType(type);
	}
	
	public <T extends BaseResponse> T checkGet(String location, Map<String, String> queryParams, Class<T> type) {
		this.checkMethod("GET");
		this.checkLocation(location);
		this.checkMap(this.queryParams, queryParams);
		return this.checkType(type);
	}
	
	public <T extends BaseResponse> T checkDelete(String location, Map<String, String> queryParams, Class<T> type) {
		this.checkMethod("DELETE");
		this.checkLocation(location);
		this.checkMap(this.queryParams, queryParams);
		return this.checkType(type);
	}
	
	public <T extends BaseResponse> T checkPut(String location, Map<String, String> body, Map<String, String> queryParams, Class<T> type) {
		this.checkMethod("PUT");
		this.checkLocation(location);
		this.checkMap(this.body, body);
		this.checkMap(this.queryParams, queryParams);
		return this.checkType(type);
	}
}