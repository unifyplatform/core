package se.unify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FirstIntegrationTest {

	@Before
	public void setUp() {
		// find out the number of invoices there are in the system
	}
	
	@After
	public void tearDown() {
		// Close possible DB-connections etc
	}
	
	@Test
	/**
	 * This test case is supposed to run on a complete installation
	 * Including queuehandlers, database etc
	 * 
	 * At the moment it is really stupid - and is only used to verify the gradle testing features (separating unit tests and integration tests)
	 * 
	 */
	public void alwaysOKTest() {
		// Slå upp informationen i routes.xml
		// Använd samma i databasen - före och efter
		assert true;
	}

}
