package com.digitalBorderlands.cityHall.data.comm;

import java.util.Map;

import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;

/**
 * Internal class, exposed for the purposes of testing.
 * 
 * @author Alex
 */
public interface Client {
	<T extends BaseResponse> T post(String location, Map<String,String> body, Map<String, String> queryParams, Class<T> type) throws CityHallException;
	
	<T extends BaseResponse> T get(String location, Map<String, String> queryParams, Class<T> type) throws CityHallException;
	
	<T extends BaseResponse> T delete(String location, Class<T> type) throws CityHallException;
	
	<T extends BaseResponse> T put(String location, Map<String, String> body, Class<T> type) throws CityHallException;
	
	void open(ClientConfig config) throws CityHallException;
	
	void close() throws CityHallException;
}
