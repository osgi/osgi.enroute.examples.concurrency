package osgi.enroute.examples.concurrency.events;

import java.io.Closeable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class EventTest extends OSGiTestCase {


	public void testGoodEvents() throws Exception {
		EventLoop loop = getService( EventLoop.class);
		Semaphore s = new Semaphore(0);
		try( Closeable c = register(EventHandler.class, (n)-> {
			s.release();
		})) {
			loop.dispatch(1);
		}
		assertTrue(s.tryAcquire(1,TimeUnit.SECONDS));
	}

	public void testBadEvents() throws Exception {
		EventLoop loop = getService( EventLoop.class);
		Semaphore s = new Semaphore(0);
		try( Closeable c = register(EventHandler.class, (n)-> {
			s.release();
			throw new Exception();
		})) {
			loop.dispatch(1);
			assertTrue(s.tryAcquire(1,TimeUnit.SECONDS));
			sleep(100);
			
			//
			// Since we're blacklisted we should not receive 
			// the dispatch
			//
			
			loop.dispatch(1);
			assertFalse(s.tryAcquire(1,TimeUnit.SECONDS)); 
		}
	}
}
