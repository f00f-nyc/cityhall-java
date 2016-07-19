package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.digitalBorderlands.cityHall.data.comm.Expected;
import com.digitalBorderlands.cityHall.data.comm.MockClient;
import com.digitalBorderlands.cityHall.data.comm.Responses;

public class EnvironmentsTest {
	
	@Test
	public void getDefaultEnvironment() throws Exception {
		String location = String.format("auth/user/%s/default/", SettingsTest.GetHostname());
		Expected auth = new Expected(Responses.ok(), null);
		Expected def = new Expected(Responses.defaultEnvironment(), "GET", location, null);
		
		MockClient.withRawResponses(new Expected[] { auth, def });
		Settings settings = new Settings();
		Assert.assertEquals(Responses.defaultEnvironment().Value, settings.environments.getDefaultEnviornment());
	}
	
	@Test
	public void setDefaultEnvironment() throws Exception {
		String location = String.format("auth/user/%s/default/", SettingsTest.GetHostname());
		HashMap<String,String> body = new HashMap<String,String>();
		body.put("env", "qa");
		MockClient.withFirstCallAfterLogin(Responses.ok(), "POST", location, body);
		
		Testable setDefaultEnv = settings -> {
			settings.environments.setDefaultEnvironment("qa");
			Assert.assertEquals("qa", settings.environments.getDefaultEnviornment());
		};
		
		setDefaultEnv.run(new Settings());
		
		ErrorTester.logOutWorks(setDefaultEnv);
	}
}
