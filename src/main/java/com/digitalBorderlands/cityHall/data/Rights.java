package com.digitalBorderlands.cityHall.data;

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
}
