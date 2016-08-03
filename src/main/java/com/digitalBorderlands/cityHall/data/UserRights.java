package com.digitalBorderlands.cityHall.data;

public class UserRights {
	public String environment;
	public Rights rights;
	
	public UserRights() { }
	
	public UserRights(String user, Integer rights) {
		this.environment = user;
		this.rights = com.digitalBorderlands.cityHall.data.Rights.ALL.get(rights);
	}
}
