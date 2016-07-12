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
}