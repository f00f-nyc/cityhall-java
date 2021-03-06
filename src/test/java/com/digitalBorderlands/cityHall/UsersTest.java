package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.digitalBorderlands.cityHall.data.Rights;
import com.digitalBorderlands.cityHall.data.UserInfo;
import com.digitalBorderlands.cityHall.data.UserRights;
import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.data.responses.UserResponse;
import com.digitalBorderlands.cityHall.exceptions.InvalidRequestException;
import com.digitalBorderlands.cityHall.impl.Container;
import com.digitalBorderlands.cityHall.impl.MockClient;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Container.client.class})
public class UsersTest {
	
	@Test
	public void getUser() throws Exception {
		String location = "/auth/user/test_user/";
		UserResponse testUserResponse = Responses.userInfo();
		MockClient.withGetCallAfterLogin(testUserResponse, location, null);
		
		Testable getUser = settings -> {
			UserInfo testUser = settings.users.getUser("test_user");
			
			Assert.assertEquals(testUserResponse.environments.size(), testUser.rights.length);
			Object[] envSet = testUserResponse.environments.entrySet().toArray();
			String[] keys = testUserResponse.environments.keySet().toArray(new String[envSet.length]);
			Integer[] values = testUserResponse.environments.values().toArray(new Integer[envSet.length]);
			
			for (int i=0; i<envSet.length; i++) {
				UserRights userResponse = testUser.rights[i];
				Assert.assertEquals(keys[i], userResponse.environment);
				Assert.assertEquals(values[i].intValue(), userResponse.rights.intValue());
			}
		};
		
		getUser.run(Settings.create());
		ErrorTester.logOutWorks(getUser);
	}
	
	@Test(expected=InvalidRequestException.class)
	public void createUserDoesntWorkWithSelf() throws Exception {
		MockClient.withFirstCallAfterLogin(Responses.ok(), "POST");
		Settings.create().users.createUser("some_user", "password");
	}
	
	@Test
	public void createUser() throws Exception {
		String location = "auth/user/a_new_user/";
		HashMap<String, String> expected = new HashMap<String, String>();
		expected.put("passhash", "");
		MockClient.withFirstCallAfterLogin(Responses.ok(), "POST", location, expected);
		Settings.create().users.createUser("a_new_user", "");
		ErrorTester.logOutWorks(settings -> settings.users.createUser("test", "pass"));
	}
	
	@Test
	public void deleteUser() throws Exception {
		MockClient.withFirstCallAfterLogin(Responses.ok(), "DELETE", "auth/user/a_new_user/", null);
		Settings.create().users.deleteUser("a_new_user");
		ErrorTester.logOutWorks(settings -> settings.users.deleteUser("a_new_user"));
	}
	
	@Test
	public void grant() throws Exception {
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("env", "dev");
		body.put("user", "a_new_user");
		body.put("rights", "2");
		MockClient.withFirstCallAfterLogin(Responses.ok(), "POST", "auth/grant/", body);
		Settings.create().users.grant("a_new_user", "dev", Rights.ReadProtected);
		ErrorTester.logOutWorks(settings -> settings.users.grant("user", "env", Rights.None));
	}
}
