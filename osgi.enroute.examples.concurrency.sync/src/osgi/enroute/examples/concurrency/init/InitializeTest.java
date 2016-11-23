package osgi.enroute.examples.concurrency.init;

import org.osgi.framework.BundleContext;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class InitializeTest extends OSGiTestCase {
	
	BundleContext context;
	
	public void testWork() throws Exception {
		Initialize init= (Initialize) getService(Work.class);
		assertFalse(init.promise.isDone());
		init.work();
		assertTrue(init.promise.isDone());
	}

	public void testClose() throws Exception {
		Initialize init= (Initialize) getService(Work.class);
		assertFalse(init.promise.isDone());
		tearDown();
		assertFalse(init.closed);
		sleep(1000);
		assertFalse(init.closed);
		sleep(2000);
		assertTrue(init.closed);
	}

}
