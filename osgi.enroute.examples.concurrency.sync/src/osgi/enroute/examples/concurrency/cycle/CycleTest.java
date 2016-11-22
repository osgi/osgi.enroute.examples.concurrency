package osgi.enroute.examples.concurrency.cycle;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class CycleTest extends OSGiTestCase {



	public void testCycle() throws Exception {
		
		Bottom bottom = getService(Bottom.class);
		
		Top top = getService(Top.class);
		
		assertEquals( top, bottom.circularReferenceToTop);
		assertTrue( top.circularReferenceToBottoms.contains(bottom));
	}

}
