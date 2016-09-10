package com.digitalBorderlands.cityHall;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.digitalBorderlands.cityHall.data.Child;
import com.digitalBorderlands.cityHall.data.Children;
import com.digitalBorderlands.cityHall.data.History;
import com.digitalBorderlands.cityHall.data.LogEntry;
import com.digitalBorderlands.cityHall.data.Value;
import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.ChildrenResponse;
import com.digitalBorderlands.cityHall.data.responses.HistoryResponse;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;
import com.digitalBorderlands.cityHall.exceptions.CityHallException;
import com.digitalBorderlands.cityHall.impl.Path;
import com.digitalBorderlands.cityHall.impl.SettingsClient;

/**
 * All Values-related functionality
 * 
 * @author Alex
 *
 */
public abstract class Values {
	protected SettingsClient parent;
	
	/**
	 * The most often used get(), duplicated in Settings::get()
	 * Will use the default environment, and the most appropriate override.
	 * 
	 * @param path         The path to the value to be retrieved.
	 * @return the value from the server, or null if the current logged in user doesn't have access or the value doesn't exist
	 * @throws CityHallException
	 */
	public String get(String path) throws CityHallException {
		return this.get(path, null, null);
	}
	
	/**
	 * The most often used get(), duplicated in Settings::get()
	 * Will use the default environment
	 * 
	 * @param path         The path to the value to be retrieved.
	 * @param override     The specific override to use. If null, then use most appropriate. 
	 * @return the value from the server, or null if the current logged in user doesn't have access or the value doesn't exist
	 * @throws CityHallException
	 */
	public String getOverride(String path, String override) throws CityHallException {
		return this.get(path, null, override);
	}
	
	/**
	 * The most often used get(), duplicated in Settings::get()
	 * Will use the default environment
	 * 
	 * @param path         The path to the value to be retrieved.
	 * @param environment  The specific environment to use. If null, then use default. 
	 * @return the value from the server, or null if the current logged in user doesn't have access or the value doesn't exist
	 * @throws CityHallException
	 */
	public String getEnvironment(String path, String environment) throws CityHallException {
		return this.get(path, environment, null);
	}
	
	/**
	 * The most often used get(), duplicated in Settings::get()
	 * Will use the default environment
	 * 
	 * @param path         The path to the value to be retrieved.
	 * @param environment  The specific environment to use. If null, then use default.
	 * @param override     The specific override to use. If null, then use most appropriate.
	 * @return the value from the server, or null if the current logged in user doesn't have access or the value doesn't exist
	 * @throws CityHallException
	 */
	public String get(String path, String environment, String override) throws CityHallException {
		return this.getFull(path, environment, override).value;
	}
	
	/**
	 * Return the full Value at the path, including protect value.
	 * 
	 * @param path         The path to the value to be retrieved.
	 * @param environment  The specific environment to use. If null, then use default.
	 * @param override     The specific override to use. If null, then use most appropriate.
	 * @return A Value object. If the `value` is null, then the current logged in user lacks permissions to access the given path
	 * @throws CityHallException
	 */
	public Value getFull(String path, String environment, String override) throws CityHallException {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		if (StringUtils.isNotBlank(override)) {
			queryParams.put("override", override);
		}
		
		if (StringUtils.isBlank(environment)) {
			environment = this.parent.getCachedDefaultEnviornment();
		}
		String location = String.format("env/%s%s", environment, Path.sanitize(path));
		ValueResponse value = this.parent.get(location, queryParams, ValueResponse.class);
		return new Value(value.value, value.protect);
	}
	
	/**
	 * Return the children for a given path. Note that only default override may have children.
	 * 
	 * @param path         The path to the value to be queried.
	 * @param environment  The specific environment to use.
	 * @return A Children object with all the visible Children to the current logged in user 
	 * @throws CityHallException
	 */
	public Children getChildren(String path, String environment) throws CityHallException {
		String sanitizedPath = Path.sanitize(path);
		String location = String.format("env/%s%s", environment, sanitizedPath);
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("viewchildren", "true");
		ChildrenResponse resp = this.parent.get(location, queryParams, ChildrenResponse.class);
		Children ret = new Children();
		ret.path = resp.path;
		ret.children = resp.children.toArray(new Child[resp.children.size()]);
		return ret;
	}
	
	/**
	 * If the current logged in user has Read Protected permissions or more, return a complete audit trail of the given path
	 * 
	 * @param path         The path to the value to be queried.
	 * @param environment  The specific environment to use.
	 * @param override     The specific override to use, if null, will retrieve default override
 	 * @return
	 * @throws CityHallException
	 */
	public History getHistory(String path, String environment, String override) throws CityHallException {
		String sanitizedPath = Path.sanitize(path);
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
		
	/**
	 * Set a value and its protect value, simultaneously
	 * 
	 * @param path         The path to the value to be queried.
	 * @param environment  The specific environment to use.
	 * @param override     The specific override to use, if null, will use default override
	 * @param value        The new value to set at the given path
	 * @param protect      The new protect value to set at the given path
	 * @throws CityHallException
	 */
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
		String location = String.format("env/%s%s", environment, Path.sanitize(path));		
		this.parent.post(location, body, queryParams, BaseResponse.class);
	}
	/**
	 * Set a value.
	 * 
	 * @param path         The path to the value to be queried.
	 * @param environment  The specific environment to use.
	 * @param override     The specific override to use, if null, will use default override
	 * @param value        The new value to set at the given path
	 * @throws CityHallException
	 */
	public void set(String path, String environment, String override, String value) throws CityHallException {
		this.set(path, environment, override, value, null);
	}
	
	/**
	 * Set the protect bit
	 * 
	 * @param path         The path to the value to be queried.
	 * @param environment  The specific environment to use.
	 * @param override     The specific override to use, if null, will use default override
	 * @param protect      The new protect value to set at the given path
	 * @throws CityHallException
	 */
	public void set(String path, String environment, String override, boolean protect) throws CityHallException {
		this.set(path, environment, override, null, protect);
	}

	/**
	 * Delete a value
	 * 
	 * @param path         The path to the value to be queried.
	 * @param environment  The specific environment to use.
	 * @param override     The specific override to use, if null, will use default override
	 * @throws CityHallException
	 */
	public void delete(String path, String environment, String override) throws CityHallException {		
		String overrideSend = StringUtils.isEmpty(override) ? "" : override;
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("override", overrideSend);
		
		String location = String.format("env/%s%s", environment, Path.sanitize(path));		
		this.parent.delete(location, BaseResponse.class);
	}
}
