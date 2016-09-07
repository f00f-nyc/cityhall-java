package com.digitalBorderlands.cityHall.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for Password. MD5 Hashing with City Hall conventions
 */
public class PasswordTest {
    
	@Test
    public void emptyPasswordIsEmpty()
    {
		String hashed = Password.hash("");
		assertEquals("Empty password shouldn't be hashed", "", hashed);
    }
	
	@Test
	public void nonEmptyPasswordShouldBeHashed()
	{
		String hashed = Password.hash("password");
		assertTrue("Non empty password should be hashed", hashed.length() > 0);
	}
}
