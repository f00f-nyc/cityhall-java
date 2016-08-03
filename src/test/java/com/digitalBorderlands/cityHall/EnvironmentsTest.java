package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.digitalBorderlands.cityHall.data.EnvironmentInfo;
import com.digitalBorderlands.cityHall.data.EnvironmentRights;
import com.digitalBorderlands.cityHall.data.comm.Expected;
import com.digitalBorderlands.cityHall.data.comm.MockClient;
import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.data.responses.EnvironmentResponse;

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
	
	@Test
	public void getEnvironment() throws Exception {
		String location = "auth/env/dev/";
		EnvironmentResponse devResponse = Responses.devEnvironment();
		MockClient.withFirstCallAfterLogin(devResponse, "GET", location, null);
		
		Testable getEnvironment = settings -> {
			EnvironmentInfo dev = settings.environments.get("dev");
			
			Assert.assertEquals(devResponse.Users.size(), dev.rights.length);
			Object[] devSet = devResponse.Users.entrySet().toArray();
			String[] keys = devResponse.Users.keySet().toArray(new String[devSet.length]);
			Integer[] values = devResponse.Users.values().toArray(new Integer[devSet.length]);
			
			for (int i=0; i<devSet.length; i++) {
				EnvironmentRights envResponse = dev.rights[i];
				Assert.assertEquals(keys[i], envResponse.user);
				Assert.assertEquals(values[i].intValue(), envResponse.rights.intValue());
			}
		};
		
		getEnvironment.run(new Settings());
		ErrorTester.logOutWorks(getEnvironment);
	}
	
	@Test
	public void createEnvironment() throws Exception {
		String location = "auth/env/qa/";
		MockClient.withFirstCallAfterLogin(Responses.ok(), "POST", location, null);
		
		Testable createEnvironment = settings -> {
			settings.environments.create("qa");
		};
		
		createEnvironment.run(new Settings());
		ErrorTester.logOutWorks(createEnvironment);		
	}
}
