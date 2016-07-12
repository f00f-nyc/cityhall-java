package com.digitalBorderlands.cityHall.data.comm;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.localserver.LocalServerTestBase;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.picocontainer.parameters.ConstantParameter;

import com.digitalBorderlands.cityHall.config.Container;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.exceptions.ErrorFromCityHallException;
import com.digitalBorderlands.cityHall.exceptions.InvalidRequestException;


public class ClientTest extends LocalServerTestBase {
	
	public class SimpleService implements HttpRequestHandler {
		public SimpleService(String response, int statusCode) {
			this(response, statusCode, null);
		}
		
		public SimpleService(String response, int statusCode, String expectedBody) {
			super();
			this.response = response;
			this.statusCode = statusCode;
			this.expectedBody = expectedBody;
		}
		
		private String response;
		private int statusCode;
		private String expectedBody;

		@Override
		public void handle(
				final HttpRequest request,
				final HttpResponse response,
				final HttpContext context) throws HttpException, IOException {
			response.setStatusCode(this.statusCode);
			
			if ((this.expectedBody != null) && (request instanceof HttpEntityEnclosingRequest)) {
			    HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
			    String body = EntityUtils.toString(entity);
			    Assert.assertEquals(this.expectedBody, body);			    
			}
			
			final StringEntity entity = new StringEntity(this.response);
			response.setEntity(entity);
		}
	}

	public String register(String message, int statusCode) throws Exception {
		this.serverBootstrap.registerHandler("*", new SimpleService(message, statusCode));
		this.clientBuilder.build();  // is this needed?
		HttpHost target = this.start();
		return String.format("http://localhost:%d", target.getPort());
	}
	
	public String register(String message, int statusCode, String expectedBody) throws Exception {
		this.serverBootstrap.registerHandler("*", new SimpleService(message, statusCode, expectedBody));
		this.clientBuilder.build(); // is this needed?
		HttpHost target = this.start();
		return String.format("http://localhost:%d", target.getPort());
	}
	
	@Test
	public void getClientUsingContainer() throws Exception {
		String success = "{\"Response\":\"Ok\"}";
		String url = this.register(success, HttpStatus.SC_OK);
		
		Container.Self.removeComponent(Client.class);
		Container.Self.addComponent(Client.class, Client.class, new ConstantParameter(url));
	
		Client client = Container.Self.getComponent(Client.class);
		BaseResponse response = client.get("/some_route", BaseResponse.class);
		Assert.assertEquals("Ok", response.Response);
		client.close();
	}
	
	@Test
	public void getReturnsSuccess() throws Exception {
		String success = "{\"Response\":\"Ok\"}";
		String url = this.register(success, HttpStatus.SC_OK);
		Client client = new Client(url);
		BaseResponse response = client.get("/some_route", BaseResponse.class);
		Assert.assertEquals("Ok", response.Response);
		Assert.assertTrue(response.Message == null); 
		client.close();
	}
	
	@Test
	public void postReturnsSuccess() throws Exception {
		String success = "{\"Response\":\"Ok\"}";
		String url = this.register(success, HttpStatus.SC_OK, "{\"key\":\"value\"}");
		Client client = new Client(url);
		
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("key", "value");
		
		BaseResponse response = client.post("/", body, BaseResponse.class);
		Assert.assertEquals("Ok", response.Response);
		Assert.assertTrue(response.Message == null);
		client.close();
	}
	
	@Test(expected=ErrorFromCityHallException.class)
	public void getReturnsFailure() throws Exception {
		String message = "Some failure message";
		String failure = "{\"Response\":\"Failure\",\"Message\":\""+message+"\"}";
		String url = this.register(failure, HttpStatus.SC_OK);
		Client client = new Client(url);
		try {
			client.get("/some_route", BaseResponse.class);
		} catch (Exception ex) {
			Assert.assertEquals(message, ex.getMessage());
			client.close();
			throw ex;
		}
	}
	
	@Test(expected=ErrorFromCityHallException.class)
	public void postReturnsFailure() throws Exception {
		String message = "Some failure message";
		String failure = "{\"Response\":\"Failure\",\"Message\":\""+message+"\"}";
		String url = this.register(failure, HttpStatus.SC_OK);
		Client client = new Client(url);
		try {
			client.post("/some_route", new HashMap<String,String>(), BaseResponse.class);
		} catch (Exception ex) {
			Assert.assertEquals(message, ex.getMessage());
			client.close();
			throw ex;
		}
	}

	@Test(expected=InvalidRequestException.class)
	public void getReturns507() throws Exception {
		String url = this.register("Some random error mesage", HttpStatus.SC_INSUFFICIENT_STORAGE);
		Client client = new Client(url);
		try {
			client.get("/some_route", BaseResponse.class);
		} catch (Exception ex) {
			client.close();
			throw ex;
		}
	}
	
	@Test(expected=InvalidRequestException.class)
	public void postReturns507() throws Exception {
		String url = this.register("Some random error mesage", HttpStatus.SC_INSUFFICIENT_STORAGE);
		Client client = new Client(url);
		try {
			client.post("/some_route", new HashMap<String, String>(), BaseResponse.class);
		} catch (Exception ex) {
			client.close();
			throw ex;
		}
	}
}
