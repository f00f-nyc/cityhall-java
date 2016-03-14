package com.digitalBorderlands.cityHall;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Unit test for Password. MD5 Hashing with City Hall conventions
 */
public class PasswordTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PasswordTest( String testName )
    {
        super( testName );
    }
	
    public void testEmptyPasswordIsEmpty()
    {
		String hashed = Password.hash("");
        assertEquals( "Empty password shouldn't be hashed", "", hashed );
    }
	
	public void testNonEmptyPasswordShouldBeHashed()
	{
		String hashed = Password.hash("password");
        assertNotSame( "Non empty password should be hashed", 0, hashed.length() );
	}
}
