package com.digitalBorderlands.cityHall.data.comm;

import com.digitalBorderlands.cityHall.data.responses.BaseResponse;
import com.digitalBorderlands.cityHall.data.responses.ValueResponse;

public class Responses {
	public static BaseResponse ok() {
		BaseResponse ret = new BaseResponse();
		ret.Message = "";
		ret.Response = "Ok";
		return ret;
	}
	
	public static ValueResponse defaultEnvironment() {
		ValueResponse ret = new ValueResponse();
		ret.Message = null;
		ret.Response = "Ok";
		ret.Value = "dev";
		return ret;
	}
}
