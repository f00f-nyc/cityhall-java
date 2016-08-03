package com.digitalBorderlands.cityHall.data.comm;

import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.EnvironmentResponse;
import com.digitalBorderlands.cityHall.data.responses.UserResponse;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;
import com.google.gson.Gson;

public class Responses {
	private static Gson gson = new Gson();
	
	public static BaseResponse ok() {
		BaseResponse ret = new BaseResponse();
		ret.Message = "";
		ret.Response = "Ok";
		return ret;
	}
	
	public static BaseResponse notOk() {
		BaseResponse ret = new BaseResponse();
		ret.Message = "Some failure message";
		ret.Response = "Failure";
		return ret;
	}
	
	public static ValueResponse defaultEnvironment() {
		ValueResponse ret = new ValueResponse();
		ret.Message = null;
		ret.Response = "Ok";
		ret.Value = "dev";
		return ret;
	}
	
	public static <T extends BaseResponse> T responseFromJson(String json, Class<T> type) {
		return Responses.gson.fromJson(json, type);
	}
	
	public static EnvironmentResponse devEnvironment() {
		return Responses.responseFromJson(
				"{\"Response\": \"Ok\", \"Users\": {\"test_user\": \"4\", \"some_user\": \"1\", \"cityhall\": \"4\"}}",
				EnvironmentResponse.class
		);
	}
	
	public static UserResponse userInfo() {
		return Responses.responseFromJson(
				"{\"Response\": \"Ok\", \"Environments\": {\"dev\": 4, \"auto\": 1, \"users\": 1}}",
				UserResponse.class
		);
	}
}
