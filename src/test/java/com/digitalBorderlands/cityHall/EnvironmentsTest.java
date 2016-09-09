package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.digitalBorderlands.cityHall.data.EnvironmentInfo;
import com.digitalBorderlands.cityHall.data.EnvironmentRights;
import com.digitalBorderlands.cityHall.data.comm.Expected;
import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.data.responses.EnvironmentResponse;
import com.digitalBorderlands.cityHall.impl.Container;
import com.digitalBorderlands.cityHall.impl.MockClient;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Container.client.class})
public class EnvironmentsTest {
	
	@Test
	public void getDefaultEnvironment() throws Exception {
		String location = "auth/user/some_user/default/";
		Expected auth = new Expected(Responses.ok(), null);
		Expected def = new Expected(Responses.defaultEnvironment(), "GET", location, null);
		
		MockClient.withRawResponses(new Expected[] { auth, def });
		Settings settings = Settings.create();
		Assert.assertEquals(Responses.defaultEnvironment().value, settings.environments.getDefaultEnviornment());
	}
	
	@Test
	public void setDefaultEnvironment() throws Exception {
		String location = "auth/user/some_user/default/";
		HashMap<String,String> body = new HashMap<String,String>();
		body.put("env", "qa");
		MockClient.withFirstCallAfterLogin(Responses.ok(), "POST", location, body);
		
		Testable setDefaultEnv = settings -> {
			settings.environments.setDefaultEnvironment("qa");
			Assert.assertEquals("qa", settings.environments.getDefaultEnviornment());
		};
		
		setDefaultEnv.run(Settings.create());
		ErrorTester.logOutWorks(setDefaultEnv);
	}
	
	@Test
	public void getEnvironment() throws Exception {
		String location = "auth/env/dev/";
		EnvironmentResponse devResponse = Responses.devEnvironment();
		MockClient.withGetCallAfterLogin(devResponse, location, null);
		
		Testable getEnvironment = settings -> {
			EnvironmentInfo dev = settings.environments.get("dev");
			
			Assert.assertEquals(devResponse.users.size(), dev.rights.length);
			Object[] devSet = devResponse.users.entrySet().toArray();
			String[] keys = devResponse.users.keySet().toArray(new String[devSet.length]);
			Integer[] values = devResponse.users.values().toArray(new Integer[devSet.length]);
			
			for (int i=0; i<devSet.length; i++) {
				EnvironmentRights envResponse = dev.rights[i];
				Assert.assertEquals(keys[i], envResponse.user);
				Assert.assertEquals(values[i].intValue(), envResponse.rights.intValue());
			}
		};
		
		getEnvironment.run(Settings.create());
		ErrorTester.logOutWorks(getEnvironment);
	}
	
	@Test
	public void createEnvironment() throws Exception {
		String location = "auth/env/qa/";
		MockClient.withFirstCallAfterLogin(Responses.ok(), "POST", location, null);
		
		Testable createEnvironment = settings -> {
			settings.environments.create("qa");
		};
		
		createEnvironment.run(Settings.create());
		ErrorTester.logOutWorks(createEnvironment);		
	}
}
