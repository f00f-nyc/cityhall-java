package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.digitalBorderlands.cityHall.data.Child;
import com.digitalBorderlands.cityHall.data.Children;
import com.digitalBorderlands.cityHall.data.History;
import com.digitalBorderlands.cityHall.data.LogEntry;
import com.digitalBorderlands.cityHall.data.Value;
import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.comm.Expected;
import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.data.responses.ChildrenResponse;
import com.digitalBorderlands.cityHall.data.responses.HistoryResponse;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;
import com.digitalBorderlands.cityHall.impl.Container;
import com.digitalBorderlands.cityHall.impl.MockClient;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Container.client.class})
public class ValuesTest {

	@Test
	public void get() throws Exception {
		String location = String.format("env/%s/app1/value1/", Responses.defaultEnvironment().value);
		MockClient.withGetCallAfterLogin(Responses.val1(), location, new HashMap<String, String>());
		String value = Settings.create().values.get("app1/value1");
		Assert.assertEquals(Responses.val1().value, value);
		ErrorTester.logOutWorks(settings -> settings.values.get("value/"));
	}
	
	@Test
	public void getFull() throws Exception {
		ValueResponse val = Responses.val1();
		MockClient.withGetCallAfterLogin(val, null, null);
		Value fromServer = Settings.create().values.getFull("some_path", null, null);
		Assert.assertNull(fromServer.protect);
		Assert.assertEquals(val.value, fromServer.value);
		
		val.protect = true;
		MockClient.withGetCallAfterLogin(val, null, null);
		fromServer = Settings.create().values.getFull("some_path", null, null);
		Assert.assertEquals(val.protect, fromServer.protect);
	}
	
	@Test
	public void getOverride() throws Exception {
		String location = String.format("env/%s/app1/value1/", Responses.defaultEnvironment().value);
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("override", "cityhall");
		MockClient.withGetCallAfterLogin(Responses.val1(), location, queryParams);
		Settings.create().values.getOverride("app1/value1", "cityhall");
	}
	
	@Test
	public void getEnvironment() throws Exception {
		String location = "env/get_env/app1/value1/";
		MockClient.withGetCallAfterLogin(Responses.val1(), location, null);
		Settings.create().values.getEnvironment("app1/value1/", "get_env");
	}
	
	@Test
	public void getWithAllParams() throws Exception {
		String location = "env/get_with_params/app1/value1/";
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("override", "cityhall");
		MockClient.withGetCallAfterLogin(Responses.val1(), location, queryParams);
		Settings.create().values.get("app1/value1", "get_with_params", "cityhall");
	}

	@Test
	public void getChildren() throws Exception {
		ChildrenResponse resp = Responses.children();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("viewchildren", "true");
		MockClient.withGetCallAfterLogin(resp, "env/qa/app1/domainA/feature_1/", queryParams);
		Children children = Settings.create().values.getChildren("app1/domainA/feature_1", "qa");
		
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
		Settings.create().values.getHistory("app1/domainA/feature_1", "qa", null);
		
		queryParams.put("override", "cityhall");
		MockClient.withGetCallAfterLogin(resp, "env/qa/app1/domainA/feature_1/", queryParams);
		History history = Settings.create().values.getHistory("app1/domainA/feature_1", "qa", "cityhall");
		
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

	private static Client mockSetWith(String value, Boolean protect, String override) {
		HashMap<String, String> body = null;
		
		if ((value != null) && (protect != null)) {
			body = new HashMap<String, String>();
			if (value != null) {
				body.put("value", value);
			}
			if (protect != null) {
				body.put("protect", protect.toString());
			}
		}

		HashMap<String, String> queryParams = null;
		if (override != null) {
			queryParams = new HashMap<String, String>();
			queryParams.put("override", override);
		}
		
		Expected expected = new Expected(Responses.ok(), "POST", "env/qa/some_value/", body, queryParams);
		return MockClient.withFirstCallAfterLogin(expected);
	}
	
	@Test
	public void set() throws Exception {
		ValuesTest.mockSetWith(null, null, null);
		Settings.create().values.set("some_value", "qa", null, null);
		
		ErrorTester.logOutWorks(settings -> settings.values.set("path", "qa", null, "value"));
	}
	
	@Test
	public void setValue() throws Exception {
		ValuesTest.mockSetWith("a value", null, null);
		Settings.create().values.set("some_value", "qa", null, "a value");
	}
	
	@Test
	public void setValueOverride() throws Exception {
		ValuesTest.mockSetWith("a value", null, "cityhall");
		Settings.create().values.set("some_value", "qa", "cityhall", "a value");
	}
	
	@Test
	public void setProtect() throws Exception {
		ValuesTest.mockSetWith(null, true, null);
		Settings.create().values.set("some_value", "qa", null, true);
	}
	
	@Test
	public void setProtectOverride() throws Exception {
		ValuesTest.mockSetWith(null, true, "cityhall");
		Settings.create().values.set("some_value", "qa", "cityhall", true);
	}
	
	@Test
	public void setValueProtect() throws Exception {
		ValuesTest.mockSetWith("a value", true, null);
		Settings.create().values.set("some_value", "qa", null, "a value", true);
	}
	
	@Test
	public void setValueProtectOverride() throws Exception {
		ValuesTest.mockSetWith("a value", true, "cityhall");
		Settings.create().values.set("some_value", "qa", "cityhall", "a value", true);
	}
	
	@Test
	public void delete() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("override", "");		
		Expected expected = new Expected(Responses.ok(), "DELETE", "env/qa/some_value/", null, queryParams);
		MockClient.withFirstCallAfterLogin(expected);
		Settings.create().values.delete("some_value", "qa", null);
		
		queryParams.put("override", "cityhall");
		expected = new Expected(Responses.ok(), "DELETE", "env/uat/app/value/", null, queryParams);
		MockClient.withFirstCallAfterLogin(expected);
		Settings.create().values.delete("/app/value/", "uat", "cityhall");
	}
}
