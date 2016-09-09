package com.digitalBorderlands.cityHall;

import org.junit.Assert;

import com.digitalBorderlands.cityHall.data.comm.Responses;
import com.digitalBorderlands.cityHall.exceptions.NotLoggedInException;
import com.digitalBorderlands.cityHall.impl.MockClient;

public class ErrorTester {
	public static void logOutWorks(Testable test) {
		try {
			MockClient.withFirstCallAfterLogin(Responses.ok(), "DELETE");
			Settings settings = Settings.create();
			settings.logout();
			test.run(settings);
		} catch (NotLoggedInException ex) {
			// suppress this error, it's expected
			return;
		} catch (Exception ex) {
			String message = String.format("Expected the call to throw a NotLoggedInException, but instead got: %s", ex.toString());
			Assert.assertTrue(message, false);
		}
		
		Assert.assertTrue("Expected the call to throw a NotLoggedInException, but nothing was thrown", false);
	}
}
