package com.digitalBorderlands.cityHall.data.responses;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;

import com.digitalBorderlands.cityHall.data.LogEntry;

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
	
	@Test
	public void historyResponseDeserialize() {
		String response = "Ok";
		String message = "some message";
	    Integer id = 1234;
	    String name = "some name";
	    String value = "some value";
	    String author = "some author";
	    Instant datetime = Instant.now();
	    String datetimeString = DateTimeFormatter.ISO_INSTANT.format(datetime);
	    Boolean active = true;
	    Boolean protect = false;
	    String override = "some override";
	    
	    String json = "{"+
					"\"Response\":\""+response+"\"," +
					"\"Message\":\""+message+"\","+
					"\"History\":[{"+
						"\"id\":"+id.toString()+","+
						"\"name\":\""+name+"\","+
						"\"value\":\""+value+"\","+
						"\"author\":\""+author+"\","+
						"\"datetime\":\""+datetimeString+"\","+
						"\"active\":"+active.toString()+","+
						"\"protect\":"+protect.toString()+","+
						"\"override\":\""+override+"\""+
					"}]"+
				"}";
	    HistoryResponse resp = BaseResponse.fromJson(json, HistoryResponse.class);
	    Assert.assertNotNull(resp);
	    Assert.assertNotNull(resp.history);
	    Assert.assertEquals(1, resp.history.size());
	    LogEntry entry = resp.history.get(0);
	    Assert.assertEquals(active, entry.active);
	    Assert.assertEquals(name, entry.name);
	    Assert.assertEquals(value, entry.value);
	    Assert.assertEquals(author, entry.author);
	    Assert.assertEquals(datetime, entry.datetime);
	    Assert.assertEquals(active, entry.active);
	    Assert.assertEquals(protect, entry.protect);
	    Assert.assertEquals(override, entry.override);
	}
}
