package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.digitalBorderlands.cityHall.data.Value;
import com.digitalBorderlands.cityHall.data.comm.MockClient;
import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;

public class ValuesTest {
	
	@Test
	public void get() throws Exception {
		String location = String.format("env/%s/app1/value1/", Responses.defaultEnvironment().value);
		MockClient.withGetCallAfterLogin(Responses.val1(), location, new HashMap<String, String>());
		String value = new Settings().values.get("app1/value1");
		Assert.assertEquals(Responses.val1().value, value);
		ErrorTester.logOutWorks(settings -> settings.values.get("value/"));
	}
	
	@Test
	public void getFull() throws Exception {
		ValueResponse val = Responses.val1();
		MockClient.withGetCallAfterLogin(val, null, null);
		Value fromServer = new Settings().values.getFull("some_path", null, null);
		Assert.assertNull(fromServer.protect);
		Assert.assertEquals(val.value, fromServer.value);
		
		val.protect = true;
		MockClient.withGetCallAfterLogin(val, null, null);
		fromServer = new Settings().values.getFull("some_path", null, null);
		Assert.assertEquals(val.protect, fromServer.protect);
	}
	
	@Test
	public void getOverride() throws Exception {
		String location = String.format("env/%s/app1/value1/", Responses.defaultEnvironment().value);
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("override", "cityhall");
		MockClient.withGetCallAfterLogin(Responses.val1(), location, queryParams);
		new Settings().values.get("app1/value1");
	}
	
	@Test
	public void getEnvironment() throws Exception {
		String location = "env/get_env/app1/value1/";
		MockClient.withGetCallAfterLogin(Responses.val1(), location, null);
		new Settings().values.getEnvironment("app1/value1/", "get_env");
	}
	
	@Test
	public void getWithAllParams() throws Exception {
		String location = "env/get_with_params/app1/value1/";
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("override", "cityhall");
		MockClient.withGetCallAfterLogin(Responses.val1(), location, queryParams);
		new Settings().values.get("app1/value1", "get_with_params", "cityhall");
	}
}
