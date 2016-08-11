package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.digitalBorderlands.cityHall.data.Child;
import com.digitalBorderlands.cityHall.data.Children;
import com.digitalBorderlands.cityHall.data.History;
import com.digitalBorderlands.cityHall.data.LogEntry;
import com.digitalBorderlands.cityHall.data.Value;
import com.digitalBorderlands.cityHall.data.comm.MockClient;
import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.data.responses.ChildrenResponse;
import com.digitalBorderlands.cityHall.data.responses.HistoryResponse;
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
		new Settings().values.getOverride("app1/value1", "cityhall");
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

	@Test
	public void getChildren() throws Exception {
		ChildrenResponse resp = Responses.children();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("viewchildren", "true");
		MockClient.withGetCallAfterLogin(resp, "env/qa/app1/domainA/feature_1/", queryParams);
		Children children = new Settings().values.getChildren("app1/domainA/feature_1", "qa");
		
		Assert.assertEquals(resp.path, children.path);
		Assert.assertEquals(resp.children.size(), children.children.length);
		
		for (int i=0; i<resp.children.size(); i++) {
			Child childResp = resp.children.get(i);
			Child childCity = children.children[i];

			Assert.assertEquals(childResp.id, childCity.id);
			Assert.assertEquals(childResp.name, childCity.name);
			Assert.assertEquals(childResp.override, childCity.override);
			Assert.assertEquals(childResp.path, childCity.path);
			Assert.assertEquals(childResp.value, childCity.value);
			Assert.assertEquals(childResp.protect, childCity.protect);
		}
		
		ErrorTester.logOutWorks(settings -> settings.values.getChildren("app1/", "env"));
	}
	
	@Test
	public void getHistory() throws Exception {
		HistoryResponse resp = Responses.history();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("viewhistory", "true");
		MockClient.withGetCallAfterLogin(resp, "env/qa/app1/domainA/feature_1/", queryParams);
		new Settings().values.getHistory("app1/domainA/feature_1", "qa", null);
		
		queryParams.put("override", "cityhall");
		MockClient.withGetCallAfterLogin(resp, "env/qa/app1/domainA/feature_1/", queryParams);
		History history = new Settings().values.getHistory("app1/domainA/feature_1", "qa", "cityhall");
		
		Assert.assertEquals(resp.history.size(), history.entries.length);
		
		for (int i=0; i<history.entries.length; i++) {
			LogEntry logResp = resp.history.get(i);
			LogEntry logCity = history.entries[i];
			
			Assert.assertEquals(logResp.id, logCity.id);
			Assert.assertEquals(logResp.name, logCity.name);
			Assert.assertEquals(logResp.value, logCity.value);
			Assert.assertEquals(logResp.author, logCity.author);
			Assert.assertEquals(logResp.datetime, logCity.datetime);
			Assert.assertEquals(logResp.active, logCity.active);
			Assert.assertEquals(logResp.protect, logCity.protect);
			Assert.assertEquals(logResp.override, logCity.override);
		}
		
		ErrorTester.logOutWorks(settings -> settings.values.getHistory("app1", "env", null));
	}
}
