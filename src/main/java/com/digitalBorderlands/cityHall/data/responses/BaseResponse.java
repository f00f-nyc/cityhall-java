package com.digitalBorderlands.cityHall.data.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class BaseResponse {
	public BaseResponse(String response, String message) {
		this.Response = response;
		this.Message = message;
	}
	
	public BaseResponse() { }
	
	public String Response;
	public String Message;
	
	public boolean isValid() {
		return this.Response.equals("Ok");
	}

	private static Gson gson = new Gson();
	
	public static <T extends BaseResponse> T fromJson(String json, Class<T> type) {
		try {
			return BaseResponse.gson.fromJson(json, type);
		} catch (JsonSyntaxException ex) {
			T ret;
			try {
				ret = type.newInstance();
				ret.Response = "Failure";
				ret.Message = String.format("Syntax exception `%s` from json: `%s`", ex.getMessage(), json);
				return ret;
			} catch (InstantiationException | IllegalAccessException e) {
				// this code is unreachable
				return null;
			}
		}
	}
	
	public String toJson() {
		return BaseResponse.gson.toJson(this, this.getClass());
	}
}
