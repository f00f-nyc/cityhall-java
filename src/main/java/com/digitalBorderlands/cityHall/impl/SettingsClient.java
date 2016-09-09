package com.digitalBorderlands.cityHall.impl;

import com.digitalBorderlands.cityHall.data.comm.Client;

public interface SettingsClient extends Client {
	String getUser();
	String getCachedDefaultEnviornment();
}
