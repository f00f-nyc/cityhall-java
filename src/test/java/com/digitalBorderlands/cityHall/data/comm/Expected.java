package com.digitalBorderlands.cityHall.data.comm;

import java.util.HashMap;
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
	
	@SuppressWarnings("unchecked")
	public <T extends BaseResponse> T check(String method, String location, HashMap<String, String> body, Class<T> type) {
		if (this.Method != null) {
			Assert.assertEquals(method.toUpperCase(), this.Method.toUpperCase());
		}
		
		if (this.Location != null) {
			Assert.assertEquals(location, this.Location);
		}
		
		if ((body != null) && (this.Body != null)) {
			Assert.assertEquals(body.size(), this.Body.size());
			
			for(Entry<String, String> entry : this.Body.entrySet()) {
				Assert.assertTrue(body.containsKey(entry.getKey()));
				Assert.assertEquals(entry.getValue(), body.get(entry.getKey()));
			}
		}

		Assert.assertEquals(type, this.Response.getClass());
		return (T)this.Response;
	}
	
	public <T extends BaseResponse> T checkGet(String location, HashMap<String, String> queryParams, Class<T> type) {
		T ret = this.check("GET", location,  null, type);
		
		if ((queryParams != null) && (this.QueryParams != null)) {
			Assert.assertEquals(queryParams.size(), this.QueryParams.size());
			
			for (Entry<String, String> entry : this.QueryParams.entrySet()) {
				Assert.assertTrue(queryParams.containsKey(entry.getKey()));
				Assert.assertEquals(entry.getValue(), queryParams.get(entry.getKey()));
			}
		}
		
		return ret;
	}
}