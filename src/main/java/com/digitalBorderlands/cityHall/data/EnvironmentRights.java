package com.digitalBorderlands.cityHall.data;

public class EnvironmentRights {
	public String user;
	public Rights rights;
	
	public EnvironmentRights() { }
	
	public EnvironmentRights(String user, Integer rights) {
		this.user = user;
		this.rights = com.digitalBorderlands.cityHall.data.Rights.ALL.get(rights);
	}
}
