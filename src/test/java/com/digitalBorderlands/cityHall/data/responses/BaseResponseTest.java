package com.digitalBorderlands.cityHall.data.responses;

import org.junit.Assert;
import org.junit.Test;

public class BaseResponseTest {

	@Test
	public void failureOnBadJson() {
		BaseResponse res = BaseResponse.fromJson("{ invalid json }", BaseResponse.class);
		Assert.assertNotNull(res);
		Assert.assertEquals(res.Response, "Failure");
		Assert.assertTrue(res.Message.indexOf("{ invalid json") > 0);
	}
	
	@Test
	public void baseResponseDeserialize() {
		String response = "Ok";
		String message = "some message";
		String json = "{"+
			"\"Response\":\""+response+"\"," +
			"\"Message\":\""+message+"\""+
		"}";
		BaseResponse res = BaseResponse.fromJson(json, BaseResponse.class);
		Assert.assertNotNull(res);
		Assert.assertEquals(response, res.Response);
		Assert.assertEquals(message, res.Message);
	}
	
	@Test
	public void valueResponseDeserialize() {
		String response = "Ok";
		String message = "some message";
		String value = "some value";
		Boolean protect = true;
		String json = "{"+
			"\"Response\":\""+response+"\"," +
			"\"Message\":\""+message+"\","+
			"\"value\":\""+value+"\","+
			"\"protect\":"+protect.toString()+
		"}";
		ValueResponse res = BaseResponse.fromJson(json, ValueResponse.class);
		Assert.assertNotNull(res);
		Assert.assertEquals(response, res.Response);
		Assert.assertEquals(message, res.Message);
		Assert.assertEquals(value, res.value);
		Assert.assertEquals(protect, res.protect);
	}
}
