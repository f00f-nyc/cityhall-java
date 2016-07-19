package com.digitalBorderlands.cityHall.data.comm;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.exceptions.ErrorFromCityHallException;
import com.digitalBorderlands.cityHall.exceptions.InvalidRequestException;
import com.digitalBorderlands.cityHall.exceptions.InvalidResponseException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;;

public class Client {
	public Client(String apiUrl) {
		this.target = null;
		final SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .build();
        this.connManager = new PoolingHttpClientConnectionManager();
        this.clientBuilder = HttpClientBuilder.create()
                .setDefaultSocketConfig(socketConfig)
                .setConnectionManager(this.connManager);
		this.client = this.clientBuilder.build();
		this.gson = new Gson();
		this.target = HttpHost.create(apiUrl);
		this.open = true;
	}
	
	public void close() {
		try {
			this.client.close();
			this.open = false;
		} catch (IOException e) {
			// suppress this error
		}
	}
	
	private HttpHost target;
	private CloseableHttpClient client = null;
	protected Boolean open;
	protected Gson gson;
    protected PoolingHttpClientConnectionManager connManager;
    protected HttpClientBuilder clientBuilder;
    
    public Boolean isOpen() {
    	return this.open;
    }
    
	public <T extends BaseResponse> T post(String location, HashMap<String,String> body, Class<T> type) throws CityHallException {
		int status = -1;
		String message = "";
		
		try {
			HttpPost httpPost = new HttpPost(location);
			String bodyJson = this.gson.toJson(body, body.getClass());
			httpPost.setEntity(new StringEntity(bodyJson));
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Type", "application/json");
			HttpClientContext context = HttpClientContext.create();
			final HttpResponse response = this.client.execute(this.target, httpPost, context);
		    status = response.getStatusLine().getStatusCode();
		    message = EntityUtils.toString(response.getEntity());
	    } catch(Exception e) { 
	    	throw new InvalidRequestException("Failed trying to reach: "+location.toString(), e);
	    } 
		
		if(status != 200) {
        	throw new InvalidRequestException(location.toString(), status);
        }
        
		try
		{
			T ret = this.gson.fromJson(message, type);
	        if (ret.Response.equals("Ok")) {
	        	return ret;
	        }
	        
	        throw new ErrorFromCityHallException(ret.Message);
		} catch (JsonSyntaxException e) {
	        throw new InvalidResponseException("Failed trying to reach: "+location.toString());
		} catch (ParseException e) {
			throw new InvalidResponseException("Failed trying to reach: "+location.toString()+", unable to get entity from response: "+e.getMessage());
		}	}

	public <T extends BaseResponse> T get(String location, Class<T> type) throws CityHallException {
		int status = -1;
		String message = "";
		
		try {
			HttpGet httpGet = new HttpGet(location);
			httpGet.setHeader("Accept", "application/json");
			HttpClientContext context = HttpClientContext.create();
			final HttpResponse response = this.client.execute(this.target, httpGet, context);
		    status = response.getStatusLine().getStatusCode();
		    message = EntityUtils.toString(response.getEntity());
	    } catch(Exception e) { 
	    	throw new InvalidRequestException("Failed trying to reach: "+location.toString(), e);
	    } 
		
		if(status != 200) {
        	throw new InvalidRequestException(location.toString(), status);
        }
        
		try
		{
			T ret = this.gson.fromJson(message, type);
	        if (ret.Response.equals("Ok")) {
	        	return ret;
	        }
	        
	        throw new ErrorFromCityHallException(ret.Message);
		} catch (JsonSyntaxException e) {
	        throw new InvalidResponseException("Failed trying to reach: "+location.toString());
		} catch (ParseException e) {
			throw new InvalidResponseException("Failed trying to reach: "+location.toString()+", unable to get entity from response: "+e.getMessage());
		}
	}

	public <T extends BaseResponse> T delete(String location, Class<T> type) throws CityHallException {
		int status = -1;
		String message = "";
		
		try {
			HttpDelete httpGet = new HttpDelete(location);
			httpGet.setHeader("Accept", "application/json");
			HttpClientContext context = HttpClientContext.create();
			final HttpResponse response = this.client.execute(this.target, httpGet, context);
		    status = response.getStatusLine().getStatusCode();
		    message = EntityUtils.toString(response.getEntity());
	    } catch(Exception e) { 
	    	throw new InvalidRequestException("Failed trying to reach: "+location.toString(), e);
	    } 
		
		if(status != 200) {
        	throw new InvalidRequestException(location.toString(), status);
        }
        
		try
		{
			T ret = this.gson.fromJson(message, type);
	        if (ret.Response.equals("Ok")) {
	        	return ret;
	        }
	        
	        throw new ErrorFromCityHallException(ret.Message);
		} catch (JsonSyntaxException e) {
	        throw new InvalidResponseException("Failed trying to reach: "+location.toString());
		} catch (ParseException e) {
			throw new InvalidResponseException("Failed trying to reach: "+location.toString()+", unable to get entity from response: "+e.getMessage());
		}		
	}
}
