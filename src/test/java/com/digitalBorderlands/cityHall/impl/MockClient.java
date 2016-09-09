package com.digitalBorderlands.cityHall.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.comm.ClientConfig;
import com.digitalBorderlands.cityHall.data.comm.Expected;
import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;

public class MockClient {	
	public static class ClientWithResponses implements Client {
		public ClientWithResponses(Expected [] expected) {
			this.expected = expected;
			this.index = 0;
		}
		
		private Expected[] expected;
		private int index;
		
		@Override
		public void close() { }
		
		@Override
		public void open(ClientConfig config) { }
		
		private Expected nextResponse() {
			Assert.assertTrue("Client called more times than responses were provided", this.index < this.expected.length);
			return this.expected[this.index++];
		}
		
		@Override
		public <T extends BaseResponse> T get(String location, Map<String,String> queryParams, Class<T> type) throws CityHallException {
			return this.nextResponse().checkGet(location, queryParams, type);
		}
		
		@Override
		public <T extends BaseResponse> T post(String location, Map<String, String> body, Map<String, String> queryParams, Class<T> type) throws CityHallException {
			return this.nextResponse().checkPost(location, body, queryParams, type);
		}
		
		@Override
		public <T extends BaseResponse> T delete(String location, Class<T> type) throws CityHallException {
			return this.nextResponse().checkDelete(location, null, type);
		}
		
		@Override
		public <T extends BaseResponse> T put(String location, Map<String, String> body, Class<T> type) throws CityHallException {
			return this.nextResponse().checkPut(location, body, null, type);
		}
	}
	
	private static void replaceContainerClient(Client client) {
		PowerMockito.mockStatic(Container.client.class);
		Mockito.when(Container.client.get()).thenReturn(client);
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
	
	public static Client withFirstCallAfterLogin(Expected underTest) {
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
