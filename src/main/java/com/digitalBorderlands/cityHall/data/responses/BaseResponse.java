package com.digitalBorderlands.cityHall.data.responses;

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
}
