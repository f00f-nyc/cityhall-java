package com.digitalBorderlands.cityHall.data.comm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;

import com.digitalBorderlands.cityHall.data.responses.BaseResponse;

public class Expected {
	public final BaseResponse Response;
	public final String Method;
	public final String Location;
	public final HashMap<String, String> Body;
	public final HashMap<String, String> QueryParams;
	
	public Expected() {
		this(Responses.ok(), null, null, null);
	}
	
	public Expected(BaseResponse response, String method) {
		this(response, method, null, null);
	}
	
	public Expected(BaseResponse response, String method, String location, HashMap<String, String> body) {
		this.Response = response;
		this.Method = method;
		this.Location = location;
		this.Body = body;
		this.QueryParams = null;
	}
	
	public Expected(BaseResponse response, String location, HashMap<String, String> queryParams) {
		this.Response = response;
		this.Method = "GET";
		this.Location = location;
		this.Body = null;
		this.QueryParams = queryParams;
	}
	
	private void checkMethod(String method) {
		if (this.Method != null) {
			Assert.assertEquals(method.toUpperCase(), this.Method.toUpperCase());
		}
	}
	
	private void checkLocation(String location) {
		if (this.Location != null) {
			Assert.assertEquals(location, this.Location);
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
		Assert.assertEquals(type, this.Response.getClass());
		return (T)this.Response;
	}
	
	public <T extends BaseResponse> T checkPost(String location, Map<String, String> body, Map<String, String> queryParams, Class<T> type) {
		this.checkMethod("POST");
		this.checkLocation(location);
		this.checkMap(this.Body, body);
		this.checkMap(this.QueryParams, queryParams);
		return this.checkType(type);
	}
	
	public <T extends BaseResponse> T checkGet(String location, Map<String, String> queryParams, Class<T> type) {
		this.checkMethod("GET");
		this.checkLocation(location);
		this.checkMap(this.QueryParams, queryParams);
		return this.checkType(type);
	}
	
	public <T extends BaseResponse> T checkDelete(String location, Map<String, String> queryParams, Class<T> type) {
		this.checkMethod("DELETE");
		this.checkLocation(location);
		this.checkMap(this.QueryParams, queryParams);
		return this.checkType(type);
	}
	
	public <T extends BaseResponse> T checkPut(String location, Map<String, String> body, Map<String, String> queryParams, Class<T> type) {
		this.checkMethod("PUT");
		this.checkLocation(location);
		this.checkMap(this.Body, body);
		this.checkMap(this.QueryParams, queryParams);
		return this.checkType(type);
	}
}