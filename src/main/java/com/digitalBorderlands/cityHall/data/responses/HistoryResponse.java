package com.digitalBorderlands.cityHall.data.responses;

import java.util.ArrayList;

import com.digitalBorderlands.cityHall.data.LogEntry;
import com.google.gson.annotations.SerializedName;

/**
 * Internal class, exposed for the purposes of testing.
 * 
 * @author Alex
 */
public class HistoryResponse extends BaseResponse {
	@SerializedName("History")
	public ArrayList<LogEntry> history;
}
