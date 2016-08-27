package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.digitalBorderlands.cityHall.data.Child;
import com.digitalBorderlands.cityHall.data.Children;
import com.digitalBorderlands.cityHall.data.History;
import com.digitalBorderlands.cityHall.data.LogEntry;
import com.digitalBorderlands.cityHall.data.Value;
import com.digitalBorderlands.cityHall.data.comm.Client;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.ChildrenResponse;
import com.digitalBorderlands.cityHall.data.responses.HistoryResponse;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.exceptions.NotLoggedInException;

public abstract class Values {
	protected Values(Settings parent) {
		this.parent = parent;
	}
	
	private Settings parent;
	
	public String get(String path) throws CityHallException {
		return this.get(path, null, null);
	}
	
	public String getOverride(String path, String override) throws CityHallException {
		return this.get(path, null, override);
	}
	
	public String getEnvironment(String path, String environment) throws CityHallException {
		return this.get(path, environment, null);
	}
	
	public String get(String path, String environment, String override) throws CityHallException {
		return this.getFull(path, environment, override).value;
	}
	
	public Value getFull(String path, String environment, String override) throws CityHallException {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		if (StringUtils.isNotBlank(override)) {
			queryParams.put("override", override);
		}
		
		if (StringUtils.isBlank(environment)) {
			environment = this.parent.environments.getDefaultEnviornment();
		}
		String location = String.format("env/%s%s", environment, Client.sanitizePath(path));
		ValueResponse value = this.parent.get(location, queryParams, ValueResponse.class);
		return new Value(value.value, value.protect);
	}
	
	public Children getChildren(String path, String environment) throws CityHallException {
		String sanitizedPath = Client.sanitizePath(path);
		String location = String.format("env/%s%s", environment, sanitizedPath);
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("viewchildren", "true");
		ChildrenResponse resp = this.parent.get(location, queryParams, ChildrenResponse.class);
		Children ret = new Children();
		ret.path = resp.path;
		ret.children = resp.children.toArray(new Child[resp.children.size()]);
		return ret;
	}
	
	public History getHistory(String path, String environment, String override) throws CityHallException {
		String sanitizedPath = Client.sanitizePath(path);
		String location = String.format("env/%s%s", environment, sanitizedPath);
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("viewhistory", "true");		
		if (override != null) {
			queryParams.put("override", override);
		}
		
		HistoryResponse resp = this.parent.get(location, queryParams, HistoryResponse.class);
		History ret = new History();
		ret.entries = resp.history.toArray(new LogEntry[resp.history.size()]);
		return ret;
	}
		
	public void set(String path, String environment, String override, String value, Boolean protect) throws CityHallException {
		HashMap<String, String> body = new HashMap<String, String>();
		if (value != null) {
			body.put("value", value);
		}
		if (protect != null) {
			body.put("protect", protect.toString());
		}
		HashMap<String, String> queryParams = null;
		if (override != null) {
			queryParams = new HashMap<String, String>();
			queryParams.put("override", override);
		}
		String location = String.format("env/%s%s", environment, Client.sanitizePath(path));		
		this.parent.post(location, body, queryParams, BaseResponse.class);
	}
	
	public void set(String path, String environment, String override, String value) throws CityHallException {
		throw new NotLoggedInException();
	}
	
	public void set(String path, String environment, String override, boolean protect) throws CityHallException {
		throw new NotLoggedInException();
	}

	public void delete(String path, String environment, String override) throws CityHallException {
		throw new NotLoggedInException();
	}
}
