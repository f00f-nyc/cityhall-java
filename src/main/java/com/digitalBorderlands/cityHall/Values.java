package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.digitalBorderlands.cityHall.data.Children;
import com.digitalBorderlands.cityHall.data.History;
import com.digitalBorderlands.cityHall.data.Value;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.exceptions.NotLoggedInException;

public abstract class Values {
	protected Values(Settings parent) {
		this.parent = parent;
	}
	
	private Settings parent;
	
	protected static String SanitizePath(String path) {
		if (StringUtils.isBlank(path) || path.equals("/")) {
			return "/";
		}
		
		char first = path.charAt(0);
		char last = path.charAt(path.length()-1);
		
		if ((first == '/') && (last == '/')) {
			return path;
		} else if (first == '/') {
			return String.format("%s/", path);
		} else if (last == '/') {
			return String.format("/%s", path);	
		}
		
		return String.format("/%s/", path);
	}
	
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
		String location = String.format("env/%s%s", environment, Values.SanitizePath(path));
		ValueResponse value = this.parent.get(location, ValueResponse.class);
		return new Value(value.value, value.protect);
	}
	
	public History getHistory(String path, String environment, String override) throws CityHallException {
		throw new NotLoggedInException();
	}
	
	public Children getChildren(String path, String environment, String override) throws CityHallException {
		throw new NotLoggedInException();
	}
	
	public void set(String path, String environment, String override, String value, boolean protect) throws CityHallException {
		throw new NotLoggedInException();
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
