package com.digitalBorderlands.cityHall.data.comm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
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
		
		this.apiPath = "";
		String actualUrl = apiUrl;
		if (apiUrl.toLowerCase().startsWith("http://") && apiUrl.substring(7).indexOf('/') > 0) {
			int index = apiUrl.substring(7).indexOf('/');
			this.apiPath = apiUrl.substring(7+index);
			actualUrl = apiUrl.substring(0, 7+index);
		} else if (!apiUrl.toLowerCase().startsWith("http://") && apiUrl.indexOf('/') > 0) {
			int index = apiUrl.indexOf('/');
			this.apiPath = apiUrl.substring(index);
			actualUrl = apiUrl.substring(0, index);
		}
		
		if (this.apiPath.endsWith("/")) {
			this.apiPath = this.apiPath.substring(0, this.apiPath.length()-1);
		}
		
		final SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .build();
        this.connManager = new PoolingHttpClientConnectionManager();
        this.clientBuilder = HttpClientBuilder.create()
                .setDefaultSocketConfig(socketConfig)
                .setConnectionManager(this.connManager);
		this.client = this.clientBuilder.build();
		this.target = HttpHost.create(actualUrl);
		this.open = true;
		this.context = HttpClientContext.create();
	}
	
	public void close() {
		try {
			this.client.close();
			this.open = false;
		} catch (IOException e) {
			// suppress this error
		}
	}
	
	private static Gson gson = new Gson();
	private HttpHost target;
	private CloseableHttpClient client = null;
	protected Boolean open;
    protected PoolingHttpClientConnectionManager connManager;
    protected HttpClientBuilder clientBuilder;
    protected HttpClientContext context;
    protected String apiPath;
    
    
    public Boolean isOpen() {
    	return this.open;
    }
    
    private interface serverCall {
    	public HttpResponse run() throws Exception;
    }
    
    private static String sanitizePath(String path) {
    	if ((path == null) || (path == "")) {
    		return "/";
    	}
    	
    	char first = path.charAt(0);
    	char last = path.charAt(path.length()-1);

    	if ((first == '/') && (last == '/')) {
    		return path;
    	} else if ((first != '/') && (last == '/')) {
    		return String.format("/%s", path);
    	} else if ((first == '/') && (last != '/')) {
    		return String.format("%s/", path);
    	}
    	
    	return String.format("/%s/", path);
    }
	
	private URI uriFromParts(String location, Map<String, String> queryParams) throws CityHallException {
		URIBuilder builder;
		try {
			String fullPath = String.format("%s%s", this.apiPath, Client.sanitizePath(location));
			builder = new URIBuilder(fullPath);
				
			if (queryParams != null) {
				for (Entry<String, String> entry: queryParams.entrySet()) {
					builder.addParameter(entry.getKey(), entry.getValue());
				}
			}
			
			return builder.build();
		} catch (URISyntaxException e) {
			throw new InvalidRequestException("Error attempting to build URI to reach", e);
		}
	}
    
    private <T extends BaseResponse> T run(String location, serverCall call, Class<T> type) throws CityHallException {
    	int status = -1;
    	String message = "";
    	
    	try {
    		final HttpResponse response = call.run();
		    status = response.getStatusLine().getStatusCode();
		    message = EntityUtils.toString(response.getEntity());
    	} catch (Exception e) {
    		throw new InvalidRequestException("Failed trying to reach: "+location, e);
    	} 
		
		if(status != 200) {
        	throw new InvalidRequestException(location.toString(), status);
        }
        
		try
		{
			T ret = BaseResponse.fromJson(message, type);
	        if (ret.response.equals("Ok")) {
	        	return ret;
	        }
	        
	        throw new ErrorFromCityHallException(ret.message);
		} catch (JsonSyntaxException e) {
	        throw new InvalidResponseException("Failed trying to reach: "+location.toString());
		} catch (ParseException e) {
			throw new InvalidResponseException("Failed trying to reach: "+location.toString()+", unable to get entity from response: "+e.getMessage());
		}
    }
    
	public <T extends BaseResponse> T post(String location, Map<String,String> body, Class<T> type) throws CityHallException {
			return this.post(location, body, null, type);
	}
	
	public <T extends BaseResponse> T post(String location, Map<String, String> body, Map<String, String> queryParams, Class<T> type) throws CityHallException {
		serverCall call = () -> {
			HttpPost httpPost = new HttpPost(this.uriFromParts(location, queryParams));
			String bodyJson = Client.gson.toJson(body, body.getClass());
			httpPost.setEntity(new StringEntity(bodyJson));
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Type", "application/json");
			return this.client.execute(this.target, httpPost, this.context);
		};
		
		return this.run(location, call, type);
	}

	public <T extends BaseResponse> T get(String location, Map<String, String> queryParams, Class<T> type) throws CityHallException {
		serverCall call = () -> {
			HttpGet httpGet = new HttpGet(this.uriFromParts(location, queryParams));
			httpGet.setHeader("Accept", "application/json");
			return this.client.execute(this.target, httpGet, this.context);
		};
		
		return this.run(location, call, type);
	}

	public <T extends BaseResponse> T delete(String location, Class<T> type) throws CityHallException {
		serverCall call = () -> {
			HttpDelete httpGet = new HttpDelete(this.uriFromParts(location, null));
			httpGet.setHeader("Accept", "application/json");
			return this.client.execute(this.target, httpGet, this.context);
	    };
	    
	    return this.run(location, call, type);
	}
	
	public <T extends BaseResponse> T put(String location, Map<String, String> body, Class<T> type) throws CityHallException {
		serverCall call = () -> {
			HttpPut httpPut = new HttpPut(this.uriFromParts(location, null));
			String bodyJson = Client.gson.toJson(body, body.getClass());
			httpPut.setEntity(new StringEntity(bodyJson));
			httpPut.setHeader("Accept", "application/json");
			httpPut.setHeader("Content-Type", "application/json");
			return this.client.execute(this.target, httpPut, this.context);
		};
		
		return this.run(location, call, type);
	}
}
