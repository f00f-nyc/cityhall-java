package com.digitalBorderlands.cityHall.data.responses;

import java.util.ArrayList;

import com.digitalBorderlands.cityHall.data.LogEntry;
import com.google.gson.annotations.SerializedName;

public class HistoryResponse extends BaseResponse {
	@SerializedName("History")
	public ArrayList<LogEntry> history;
}
