package com.digitalBorderlands.cityHall.data;

import java.util.HashMap;

public class EnvironmentRights {
	public String User;
	public Rights Rights;
	
	private static HashMap<Integer, Rights> rights = getRights();
	
	private static HashMap<Integer, Rights> getRights() {
		HashMap<Integer, Rights> ret = new HashMap<Integer, Rights>();
		for (Rights right : com.digitalBorderlands.cityHall.data.Rights.values()) {
			ret.put(new Integer(right.intValue()), right);
		}
		return ret;
	}
	
	public EnvironmentRights() { }
	
	public EnvironmentRights(String user, Integer rights) {
		this.User = user;
		this.Rights = EnvironmentRights.rights.get(rights);
	}
}
