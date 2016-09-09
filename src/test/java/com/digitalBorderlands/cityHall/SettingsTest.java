package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.digitalBorderlands.cityHall.data.comm.Expected;
import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.exceptions.NotLoggedInException;
import com.digitalBorderlands.cityHall.impl.Container;
import com.digitalBorderlands.cityHall.impl.MockClient;
import com.digitalBorderlands.cityHall.impl.Password;

/**
 * Unit test for Settings, the entry point for interfacing with City Hall
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Container.client.class})
public class SettingsTest {
	
	@Test
	public void settingsLogsIn() throws Exception {
		MockClient.withRawResponses(Responses.ok(), Responses.defaultEnvironment());
		Settings settings = Settings.create();
		Assert.assertTrue(settings.isLoggedIn());
		Assert.assertEquals(Responses.defaultEnvironment().value, settings.environments.getDefaultEnviornment());
	}
	
	@Test
	public void loginWithPassword() throws Exception {
		String user = "some_user";
		String password = "some_password";
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("username", user);
		body.put("passhash", Password.hash(password));
		
		Expected auth = new Expected(Responses.ok(), "POST", "auth/", body);
		Expected def = new Expected(Responses.defaultEnvironment(), "GET");
		
		MockClient.withRawResponses(new Expected[] { auth, def });
		Settings.create("http://some.url/api/", user, password);
	}
	
	@Test
	public void loginWithEmptyPassword() throws Exception {
		String user = "some_user";
		String password = "";
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("username", user);
		body.put("passhash", Password.hash(password));
		
		Expected auth = new Expected(Responses.ok(), "POST", "auth/", body);
		Expected def = new Expected(Responses.defaultEnvironment(), "GET");
		
		MockClient.withRawResponses(new Expected[] { auth, def });
		Settings.create("http://some.url/api", user, password);
	}
	
	@Test
	public void loginWithSettingsFromResources() throws Exception {
		String user = "some_user";
		String password = "thepassword";
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("username", user);
		body.put("passhash", Password.hash(password));
		
		Expected auth = new Expected(Responses.ok(), "POST", "auth/", body);
		Expected def = new Expected(Responses.defaultEnvironment(), "GET");
		
		MockClient.withRawResponses(new Expected[] { auth, def });
		Settings.create();
	}
	
	@Test
	public void logoutIsHonored() throws Exception {
		MockClient.withFirstCallAfterLogin(Responses.ok(), "DELETE", "auth/");
		Settings settings = Settings.create();
		settings.logout();
		Assert.assertFalse(settings.isLoggedIn());
	}
	
	@Test(expected=NotLoggedInException.class)
	public void testClientOpen() throws Exception {
		MockClient.withFirstCallAfterLogin(Responses.ok(), null);
		Settings settings = Settings.create();
		settings.logout();
		settings.get("some_path");
	}

	private void updatePassword(String password) throws Exception {
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("passhash", Password.hash(password));
		String location = "auth/user/some_user/";
		MockClient.withFirstCallAfterLogin(Responses.ok(), "PUT", location, body);
		Settings.create().updatePassword(password);
		ErrorTester.logOutWorks(settings -> settings.updatePassword(password));
	}
	
	@Test
	public void updatePassword() throws Exception {
		this.updatePassword("");
		this.updatePassword("password");
	}
}
