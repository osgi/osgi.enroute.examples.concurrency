package osgi.enroute.examples.concurrency.sync;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class SyncTest extends OSGiTestCase {

	/**
	 * The following test is not deterministic because it has the assumption the
	 * stop variable is cached by the core. However if you run the multiple
	 * tests then there seems to be an interaction
	 */
	public void testNormal() throws Exception {
		@SuppressWarnings("unused")
		StopLoopWithNormalVariable normal = getService(
				StopLoopWithNormalVariable.class);
		ungetAll();
		Thread.sleep(1000);
		//assertTrue(normal.isAlive());
	}

	public void testVolatile() throws Exception {
		StopLoopWithVolatileVariable vola = getService(
				StopLoopWithVolatileVariable.class);
		ungetAll();
		Thread.sleep(1000);
		assertFalse(vola.isAlive()); // this is good
	}
}
