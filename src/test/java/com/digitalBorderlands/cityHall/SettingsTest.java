package com.digitalBorderlands.cityHall;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.comm.Expected;
import com.digitalBorderlands.cityHall.data.comm.MockClient;
import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.exceptions.NotLoggedInException;

/**
 * Unit test for Settings, the entry point for interfacing with City Hall
 */
public class SettingsTest {
	static String GetHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "guest";
		}
	}
	
	@Test
	public void settingsLogsIn() throws Exception {
		MockClient.withRawResponses(Responses.ok(), Responses.defaultEnvironment());
		Settings settings = new Settings();
		Assert.assertTrue(settings.isLoggedIn());
		Assert.assertEquals(Responses.defaultEnvironment().Value, settings.environments.getDefaultEnviornment());
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
		
		Client client = MockClient.withRawResponses(new Expected[] { auth, def });
		new Settings(user, password, client);
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
		
		Client client = MockClient.withRawResponses(new Expected[] { auth, def });
		new Settings(user, password, client);
	}
	
	@Test
	public void loginWithOnlyUrl() throws Exception {
		String user = SettingsTest.GetHostname();
		String password = "";
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("username", user);
		body.put("passhash", Password.hash(password));
		
		Expected auth = new Expected(Responses.ok(), "POST", "auth/", body);
		Expected def = new Expected(Responses.defaultEnvironment(), "GET");
		
		MockClient.withRawResponses(new Expected[] { auth, def });
		new Settings();
	}
	
	@Test
	public void logoutIsHonored() throws Exception {
		MockClient.withFirstCallAfterLogin(Responses.ok(), "DELETE", "auth/");
		Settings settings = new Settings();
		settings.logout();
		Assert.assertFalse(settings.isLoggedIn());
	}
	
	@Test(expected=NotLoggedInException.class)
	public void testClientOpen() throws Exception {
		MockClient.withFirstCallAfterLogin(Responses.ok(), null);
		Settings settings = new Settings();
		settings.logout();
		settings.ensureLoggedIn();
	}
}