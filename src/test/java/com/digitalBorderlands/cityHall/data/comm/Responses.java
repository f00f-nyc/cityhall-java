package com.digitalBorderlands.cityHall.data.comm;

import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.ChildrenResponse;
import com.digitalBorderlands.cityHall.data.responses.EnvironmentResponse;
import com.digitalBorderlands.cityHall.data.responses.HistoryResponse;
import com.digitalBorderlands.cityHall.data.responses.UserResponse;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;
import com.google.gson.Gson;

public class Responses {
	private static Gson gson = BaseResponse.getDeserializer();
	
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
	
	public static ValueResponse valueResponse(String value) {
		ValueResponse ret = new ValueResponse();
		ret.Message = null;
		ret.Response = "Ok";
		ret.value = value;
		return ret;
	}
	
	public static ValueResponse defaultEnvironment() {
		return Responses.valueResponse("dev");
	}
	
	public static ValueResponse val1() {
		return Responses.valueResponse("sample value");
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
	
	public static ChildrenResponse children() {
		return Responses.responseFromJson(
				"{\"Response\": \"Ok\", \"path\":\"/app1/domainA/feature_1/\", \"children\": [ { \"id\": 302, \"name\": \"value1\", \"override\" = \"\", \"path\": \"/app1/domainA/feature_1/value1/\", \"protect\" = false, \"value\" = \"1000\"}, { \"id\": 552, \"name\": \"value1\", \"override\" = \"dev_user\", \"path\": \"/app1/domainA/feature_1/value1/\", \"protect\" = false, \"value\" = \"132\"} ] }",
				ChildrenResponse.class
		);
	}
	
	public static HistoryResponse history() {
		return Responses.responseFromJson(
				"{\"Response\":\"Ok\", \"History\": [{\"active\":false, \"override\": \"\", \"id\": 12, \"datetime\":\"2016-08-04T15:21:41.821Z\", \"protect\": false, \"name\": \"value1\", \"author\": \"cityhall\"}, {\"active\":true, \"override\": \"\", \"id\": 12, \"datetime\":\"2016-08-05T15:21:41.821Z\", \"protect\": false, \"name\": \"value2\", \"author\": \"cityhall\"}]}",
				HistoryResponse.class
		);
	}
}
