package com.digitalBorderlands.cityHall.data.comm;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.digitalBorderlands.cityHall.impl.Container;
import com.digitalBorderlands.cityHall.impl.Password;

public class ClientConfigTest {

	@Test
	public void getDefaultUsesResources() throws Exception {
		ClientConfig config = Container.getClientConfig(null, null, null);
		
		Properties prop = new Properties();
		InputStream input = null;
		
		input = ClientConfigTest.class.getClassLoader().getResourceAsStream("resources/cityhall/server.properties");
		Assert.assertNotNull("Should be able to find a server.properties file", input);
		
		prop.load(input);
		input.close();
		
		String apiUrl = prop.getProperty("url");
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");
		String passhash = Password.hash(password);
		
		Assert.assertEquals(apiUrl, config.getApiUrl());
		Assert.assertEquals(username, config.getUsername());
		Assert.assertEquals(passhash, config.getPasshash());
	}
	
	@Test
	public void setApiUrl() throws Exception {
		String url = "http://some.other.url/api/";
		ClientConfig config = Container.getClientConfig(url, null, null);
		Assert.assertEquals(url, config.getApiUrl());
	}
	
	@Test
	public void setUsername() throws Exception {
		String username = "some_other_user";
		ClientConfig config = Container.getClientConfig(null, username, null);
		Assert.assertEquals(username, config.getUsername());
	}
	
	@Test
	public void setPassword() throws Exception {
		String password = "someotherpassword";		
		ClientConfig config = Container.getClientConfig(null, null, password);
		Assert.assertEquals(Password.hash(password), config.getPasshash());
	}
}
