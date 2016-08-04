package com.digitalBorderlands.cityHall.data.comm;

import java.util.HashMap;

import org.junit.Assert;

import com.digitalBorderlands.cityHall.config.Container;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;

public class MockClient {	
	private static class ClientWithResponses extends Client {
		public ClientWithResponses(Expected [] expected) { 
			super("");
			this.expected = expected;
			this.index = 0;
		}
		
		private Expected[] expected;
		private int index;
		
		@Override
		public void close() {
			this.open = false;
		}
		
		private Expected nextResponse() {
			Assert.assertTrue("Client called more times than responses were provided", this.index < this.expected.length);
			return this.expected[this.index++];
		}
		
		@Override
		public <T extends BaseResponse> T get(String location, HashMap<String,String> queryParams, Class<T> type) throws CityHallException {
			return this.nextResponse().checkGet(location, queryParams, type);
		}
		
		@Override
		public <T extends BaseResponse> T post(String location, HashMap<String, String> body, Class<T> type) throws CityHallException {
			return this.nextResponse().check("POST", location, body, type);
		}
		
		@Override
		public <T extends BaseResponse> T delete(String location, Class<T> type) throws CityHallException {
			return this.nextResponse().check("DELETE", location, null, type);
		}
		
		@Override
		public <T extends BaseResponse> T put(String location, HashMap<String, String> body, Class<T> type) throws CityHallException {
			return this.nextResponse().check("PUT", location, body, type);
		}
	}
	
	private static void replaceContainerClient(Client client) {
		Container.Self.removeComponent(Client.class);
		Container.Self.addComponent(Client.class, client);
	}
			
	public static Client withRawResponses(BaseResponse resp1, BaseResponse resp2) {
		Expected pair1 = new Expected(resp1, null);
		Expected pair2 = new Expected(resp2, null);
		Client client = new ClientWithResponses(new Expected[] { pair1, pair2 });
		MockClient.replaceContainerClient(client);
		return client;
	}
	
	public static Client withRawResponses(Expected[] expected) {
		Client client = new ClientWithResponses(expected);
		MockClient.replaceContainerClient(client);
		return client;
	}
	
	private static Client withFirstCallAfterLogin(Expected underTest) {
		Expected login = new Expected(Responses.ok(), "POST", "auth/", null);
		Expected defaultEnvironment = new Expected(Responses.defaultEnvironment(), null, null);
		Client client = new ClientWithResponses(new Expected[] { login, defaultEnvironment, underTest });
		MockClient.replaceContainerClient(client);
		return client;
	}
	
	public static Client withFirstCallAfterLogin(BaseResponse resp, String method, String location, HashMap<String,String> body) {		
		Expected underTest = new Expected(resp, method, location, body);
		return MockClient.withFirstCallAfterLogin(underTest);
	}
	
	public static Client withFirstCallAfterLogin(BaseResponse resp, String method, String location) {
		Expected underTest = new Expected(resp, method, location, null);
		return MockClient.withFirstCallAfterLogin(underTest);
	}
	
	public static Client withGetCallAfterLogin(BaseResponse resp, String location, HashMap<String,String> queryParams) {
		Expected underTest = new Expected(resp, location, queryParams);
		return MockClient.withFirstCallAfterLogin(underTest);
	}
	
	public static Client withFirstCallAfterLogin(BaseResponse resp, String method) {
		return MockClient.withFirstCallAfterLogin(resp, method, null);
	}	
}
