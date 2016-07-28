package com.digitalBorderlands.cityHall.data.responses;

import com.google.gson.annotations.SerializedName;

public class ValueResponse extends BaseResponse {
	
	@SerializedName("value")
	public String Value;
	
	@SerializedName("protect")
	public boolean Protect;
}
