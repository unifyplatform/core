package se.unify;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SecondApplicationTest {

	SecondApplication sa = null;
	
	@Before
	public void setUp() {
		sa = new SecondApplication();
	}
	
	/**
	 * Very stupid case - just here to enable different testing filtering from the gradle scripts
	 */
	@Test
	public void testFromSQL() {
		assertEquals("Processing from input", sa.fromSql());
	}

}
