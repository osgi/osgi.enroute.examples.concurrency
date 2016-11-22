package osgi.enroute.examples.concurrency.queue;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class BackgroundTest extends OSGiTestCase {

	public void testHappyCase() throws Exception {
		Background background = getService(Background.class);
		Semaphore s = new Semaphore(0);
		background.work(() -> s.release());

		assertTrue(s.tryAcquire(1, TimeUnit.MINUTES));
	}

	public void testInCorrectThread() throws Exception {
		Background background = getService(Background.class);
		Semaphore s = new Semaphore(0);
		AtomicReference<Thread> thread = new AtomicReference<>();
		background.work(() -> {
			thread.set(Thread.currentThread());
			s.release();
		});

		assertTrue(s.tryAcquire(1, TimeUnit.MINUTES));
		assertEquals( background, thread.get());
	}
	
	public void testDieCorrectly() throws Exception {
		Background background = getService(Background.class);
		assertWait(() -> background.isAlive(), 1000);
		ungetAll();
		assertWait(() -> !background.isAlive(), 1000);
	}
	
	
	public void testThrowException() throws Exception {
		Background background = getService(Background.class);
		Semaphore s = new Semaphore(0);
		background.work(() -> {
			s.release();
			throw new RuntimeException();
		});
		assertTrue(s.tryAcquire(1, TimeUnit.MINUTES));
		assertTrue( background.isAlive());
		
		background.work(() -> s.release());

		assertTrue(s.tryAcquire(1, TimeUnit.MINUTES));
	}
	
	
	
}
