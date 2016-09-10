package com.digitalBorderlands.cityHall.data;

import java.util.HashMap;

/**
 * A user rights for an environment
 * 
 * @author Alex
 *
 */
public enum Rights {
   
	/**
	 * The user specifically has no rights to the environment
	 */
	None(0),
	
	/**
	 * The user may read non-elevated permissions values
	 */
    Read(1),
    
    /**
     * The user may read values with protect bit set to true
     */
    ReadProtected(2),
    
    /**
     * The user may write values, including setting the protect bit
     */
    Write(3),
    
    /**
     * The user may grant other permissions for this environment
     */
    Grant(4);
	
	private final int value;
	
	private Rights(int value) {
		this.value = value;
	}
	
	/**
	 * @return The enumeration value for the right
	 */
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
