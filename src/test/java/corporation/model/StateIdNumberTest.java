package corporation.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StateIdNumberTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=IllegalArgumentException.class)
	public void testStateIdNumberWrongFormat() {
		new StateIdNumber("656");
	}


	@Test
	public void testStateIdNumberPerson() {
		new StateIdNumber("790524-1415");
	}

	@Test
	public void testStateIdNumberPersonLong() {
		new StateIdNumber("710425-4680");
	}

	
	
	
	@Test
	public void testGetLongVersion() {
		
	}

}
