package com.digitalBorderlands.cityHall.data.responses;

import java.lang.reflect.Type;
import java.time.Instant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

/**
 * Internal class, exposed for the purposes of testing.
 * 
 * @author Alex
 */
public class BaseResponse {
	public BaseResponse(String response, String message) {
		this.response = response;
		this.message = message;
	}
	
	public BaseResponse() { }
	
	@SerializedName("Response")
	public String response;
	
	@SerializedName("Message")
	public String message;
	
	public boolean isValid() {
		return this.response.equals("Ok");
	}

	private static Gson gson = BaseResponse.getDeserializer();
	
	public static Gson getDeserializer() {
		return new GsonBuilder()
				.registerTypeAdapter(Instant.class, new JsonDeserializer<Instant>(){
					@Override
					public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
						return Instant.parse(json.getAsJsonPrimitive().getAsString());
				    }
				}).create();
	}
	
	public static <T extends BaseResponse> T fromJson(String json, Class<T> type) {
		try {
			return BaseResponse.gson.fromJson(json, type);
		} catch (JsonSyntaxException ex) {
			T ret;
			try {
				ret = type.newInstance();
				ret.response = "Failure";
				ret.message = String.format("Syntax exception `%s` from json: `%s`", ex.getMessage(), json);
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
