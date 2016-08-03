package com.digitalBorderlands.cityHall.data;

import java.util.HashMap;

public enum Rights {
    None(0),
    Read(1),
    ReadProtected(2),
    Write(3),
    Grant(4);
	
	private final int value;
	
	private Rights(int value) {
		this.value = value;
	}
	
	public int intValue() {
		return this.value;
	}

	static final HashMap<Integer, Rights> ALL = getRights();
	
	static HashMap<Integer, Rights> getRights() {
		HashMap<Integer, Rights> ret = new HashMap<Integer, Rights>();
		for (Rights right : Rights.values()) {
			ret.put(new Integer(right.intValue()), right);
		}
		return ret;
	}
}
