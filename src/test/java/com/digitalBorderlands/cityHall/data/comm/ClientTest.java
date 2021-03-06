package com.digitalBorderlands.cityHall.data.comm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.localserver.LocalServerTestBase;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.exceptions.ErrorFromCityHallException;
import com.digitalBorderlands.cityHall.exceptions.InvalidRequestException;
import com.digitalBorderlands.cityHall.impl.Container;


public class ClientTest extends LocalServerTestBase {
	
	public class SimpleService implements HttpRequestHandler {
		public SimpleService(String response, int statusCode, String location, String expectedBody, HashMap<String, String> expectedQueryParams, String method) {
			this.response = response;
			this.statusCode = statusCode;
			this.expectedBody = expectedBody;
			this.method = method;
			this.expectedQueryParams = expectedQueryParams;
			this.location = location;
		}
		
		private String response;
		private int statusCode;
		private String location;
		private String expectedBody;
		private String method;
		private HashMap<String, String> expectedQueryParams;

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
			
			if (this.method != null) {
			    String actualMethod = request.getRequestLine().getMethod();
				Assert.assertEquals(this.method.toLowerCase(), actualMethod.toLowerCase());
			}
			 
			if (this.expectedQueryParams != null) {
				try {
					URI uri = new URI(request.getRequestLine().getUri());
					for (NameValuePair pair : URLEncodedUtils.parse(uri, "UTF-8")) {
						Assert.assertTrue(this.expectedQueryParams.containsKey(pair.getName()));
						Assert.assertEquals(pair.getValue(), this.expectedQueryParams.get(pair.getName()));
					}
				} catch (URISyntaxException e) {
					Assert.assertTrue("Caught an error while trying to check query params: " + e.getMessage(), false);
					e.printStackTrace();
				}
			}
			
			if (this.location != null) {
				Assert.assertEquals(this.location, request.getRequestLine().getUri());
			}
			
			final StringEntity entity = new StringEntity(this.response);
			response.setEntity(entity);
		}
	}
	
	public String register(String message, int statusCode, String expectedBody, String location, HashMap<String,String> queryParams, String method) throws Exception {
		this.serverBootstrap.registerHandler("*", new SimpleService(message, statusCode, location, expectedBody, queryParams, method));
		this.clientBuilder.build(); // is this needed?
		HttpHost target = this.start();
		return String.format("http://localhost:%d", target.getPort());
	}
	
	@Test
	public void getClientUsingContainer() throws Exception {
		String success = "{\"Response\":\"Ok\"}";
		String url = this.register(success, HttpStatus.SC_OK, null, null, null, "GET");

		Client client = Container.getOpenClient(url, null, null);
		BaseResponse response = client.get("/some_route", null, BaseResponse.class);
		Assert.assertEquals("Ok", response.response);
		client.close();
	}
	
	@Test
	public void getReturnsSuccess() throws Exception {
		String success = "{\"Response\":\"Ok\"}";
		String url = this.register(success, HttpStatus.SC_OK, null, null, null, "GET");
		Client client = Container.getOpenClient(url, null, null);
		BaseResponse response = client.get("/some_route", null, BaseResponse.class);
		Assert.assertEquals("Ok", response.response);
		Assert.assertTrue(response.message == null); 
		client.close();
	}
	
	@Test
	public void getUsingQueryParams() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("override", "cityhall");
		String url = this.register(Responses.ok().toJson(), HttpStatus.SC_OK, null, null, queryParams, "GET");
		Client client = Container.getOpenClient(url, null, null);
		client.get("/some_route", queryParams, BaseResponse.class);
		client.close();
	}
	
	@Test
	public void postReturnsSuccess() throws Exception {
		String success = "{\"Response\":\"Ok\"}";
		String url = this.register(success, HttpStatus.SC_OK, "{\"key\":\"value\"}", null, null, "POST");
		Client client = Container.getOpenClient(url, null, null);
		
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("key", "value");
		
		BaseResponse response = client.post("/", body, null, BaseResponse.class);
		Assert.assertEquals("Ok", response.response);
		Assert.assertTrue(response.message == null);
		client.close();
	}
	
	@Test
	public void postUsingQueryParams() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("override", "cityhall");
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("key", "value");
		String url = this.register(Responses.ok().toJson(), HttpStatus.SC_OK, "{\"key\":\"value\"}", null, queryParams, "POST");
		Client client = Container.getOpenClient(url, null, null);
		client.post("/some_route", body, queryParams, BaseResponse.class);
		client.close();
	}
	
	@Test
	public void deleteReturnsSuccess() throws Exception {
		String success = "{\"Response\":\"Ok\"}";
		String url = this.register(success, HttpStatus.SC_OK, null, null, null, "DELETE");
		Client client = Container.getOpenClient(url, null, null);
		
		BaseResponse response = client.delete("/", BaseResponse.class);
		Assert.assertEquals("Ok", response.response);
		Assert.assertTrue(response.message == null);
		client.close();
	}
	
	@Test
	public void putReturnsSuccess() throws Exception {
		String success = "{\"Response\":\"Ok\"}";
		String url = this.register(success, HttpStatus.SC_OK, "{\"key\":\"value\"}", null, null, "PUT");
		Client client = Container.getOpenClient(url, null, null);
		
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("key", "value");
		
		BaseResponse response = client.put("/", body, BaseResponse.class);
		Assert.assertEquals("Ok", response.response);
		Assert.assertTrue(response.message == null);
		client.close();
	}
	
	@Test(expected=ErrorFromCityHallException.class)
	public void getReturnsFailure() throws Exception {
		String message = "Some failure message";
		String failure = "{\"Response\":\"Failure\",\"Message\":\""+message+"\"}";
		String url = this.register(failure, HttpStatus.SC_OK, null, null, null, "GET");
		Client client = Container.getOpenClient(url, null, null);
		try {
			client.get("/some_route", null, BaseResponse.class);
		} catch (Exception ex) {
			Assert.assertEquals(message, ex.getMessage());
			client.close();
			throw ex;
		}
	}
	
	@Test(expected=ErrorFromCityHallException.class)
	public void deleteReturnsFailure() throws Exception {
		String message = "Some failure message";
		String failure = "{\"Response\":\"Failure\",\"Message\":\""+message+"\"}";
		String url = this.register(failure, HttpStatus.SC_OK, null, null, null, "DELETE");
		Client client = Container.getOpenClient(url, null, null);
		try {
			client.delete("/some_route", BaseResponse.class);
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
		String url = this.register(failure, HttpStatus.SC_OK, null, null, null, "POST");
		Client client = Container.getOpenClient(url, null, null);
		try {
			client.post("/some_route", new HashMap<String,String>(), null, BaseResponse.class);
		} catch (Exception ex) {
			Assert.assertEquals(message, ex.getMessage());
			client.close();
			throw ex;
		}
	}
	
	@Test(expected=ErrorFromCityHallException.class)
	public void putReturnsFailure() throws Exception {
		String message = "Some failure message";
		String failure = "{\"Response\":\"Failure\",\"Message\":\""+message+"\"}";
		String url = this.register(failure, HttpStatus.SC_OK, null, null, null, "PUT");
		Client client = Container.getOpenClient(url, null, null);
		try {
			client.put("/some_route", new HashMap<String,String>(), BaseResponse.class);
		} catch (Exception ex) {
			Assert.assertEquals(message, ex.getMessage());
			client.close();
			throw ex;
		}
	}

	@Test(expected=InvalidRequestException.class)
	public void getReturns507() throws Exception {
		String url = this.register("Some random error mesage", HttpStatus.SC_INSUFFICIENT_STORAGE, null, null, null, "GET");
		Client client = Container.getOpenClient(url, null, null);
		try {
			client.get("/some_route", null, BaseResponse.class);
		} catch (Exception ex) {
			client.close();
			throw ex;
		}
	}

	@Test(expected=InvalidRequestException.class)
	public void deleteReturns507() throws Exception {
		String url = this.register("Some random error mesage", HttpStatus.SC_INSUFFICIENT_STORAGE, null, null, null, "DELETE");
		Client client = Container.getOpenClient(url, null, null);
		try {
			client.delete("/some_route", BaseResponse.class);
		} catch (Exception ex) {
			client.close();
			throw ex;
		}
	}
	
	@Test(expected=InvalidRequestException.class)
	public void postReturns507() throws Exception {
		String url = this.register("Some random error mesage", HttpStatus.SC_INSUFFICIENT_STORAGE, null, null, null, "POST");
		Client client = Container.getOpenClient(url, null, null);
		try {
			client.post("/some_route", new HashMap<String, String>(), null, BaseResponse.class);
		} catch (Exception ex) {
			client.close();
			throw ex;
		}
	}
	
	@Test(expected=InvalidRequestException.class)
	public void putReturns507() throws Exception {
		String url = this.register("Some random error mesage", HttpStatus.SC_INSUFFICIENT_STORAGE, null, null, null, "PUT");
		Client client = Container.getOpenClient(url, null, null);
		try {
			client.put("/some_route", new HashMap<String, String>(), BaseResponse.class);
		} catch (Exception ex) {
			client.close();
			throw ex;
		}
	}
	
	@Test
	public void clientCreateWithComplexUrl() throws Exception {
		String success = "{\"Response\":\"Ok\"}";
		String url = this.register(success, HttpStatus.SC_OK, null, "/api/some/path/", null, "GET");
		
		Client client = Container.getOpenClient(url+"/api/", null, null);		
		BaseResponse response = client.get("/some/path/", null, BaseResponse.class);
		Assert.assertEquals("Ok", response.response);
		Assert.assertTrue(response.message == null);
		client.close();
		
		url = url.substring(7);	//get rid of the 'http://'
		client = Container.getOpenClient(url+"/api/", null, null);
		client.get("some/path", null, BaseResponse.class);
		client.close();
	}
}
