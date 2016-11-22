package osgi.enroute.examples.concurrency.sync.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.TestCase;
import osgi.enroute.examples.concurrency.sync.util.Stopwatch;

/**
 * This test case runs a loop that increments an integer and stops the loop by
 * checking a boolean. The different test methods test out different variations
 * and time them.
 */

public class TestBeforeAfterRelation extends TestCase {

	Stopwatch			stopwatch	= new Stopwatch();
	boolean				stop;
	volatile boolean	volatileStop;
	AtomicBoolean		finished	= new AtomicBoolean(false);

	/**
	 * The most common concurrency problem I've seen, is not realizing that a
	 * field written by one thread is not guaranteed to be seen by a different
	 * thread. A common application of this:
	 * 
	 */
	public void testNoStop() throws InterruptedException {
		stop = false;
		begin(() -> {
			long tmp = 0;
			while (!stop)
				++tmp;
			return tmp;

		}, "No Stop");
		stop = true;
		report(false);
	}

	/**
	 * Using an atomic boolean will create a before/after relation so is safe
	 */
	public void testStopWithAtomicBoolean() throws InterruptedException {
		AtomicBoolean stop = new AtomicBoolean(false);
		begin(() -> {
			long tmp = 0;
			while (!stop.get())
				tmp++;
			return tmp;
		}, "Using atomic boolean");
		stop.set(true);
		report(true);
	}

	/**
	 * Using an atomic integer for counting also creates a before/after relation
	 */
	public void testSurprisingStop() throws InterruptedException {
		stop = false;
		AtomicLong result = new AtomicLong(0);

		begin(() -> {
			while (!stop)
				if (result.incrementAndGet() == 0)
					break;
			return result.get();
		}, "Surprise!");
		stop = true;
		report(true);
	}

	/**
	 * Use a synchronized block to test if we need to quit
	 */
	public void testSynchronizedStop() throws InterruptedException {
		stop = false;

		begin(() -> {
			long tmp = 0;
			while (true) {

				synchronized (TestBeforeAfterRelation.this) {
					if (stop)
						return tmp;
				}
				tmp++;
			}
		}, "Synchronized stop");
		stop = true;
		report(true);
	}

	/**
	 * Use interrupts to stop the thread
	 */
	public void testStopUsingInterrupt() throws InterruptedException {
		stop = false;
		Thread t = begin(() -> {
			long tmp = 0;
			while (!Thread.currentThread().isInterrupted())
				tmp++;
			return tmp;
		}, "Using interrupts");
		t.interrupt();
		report(true);
	}

	/**
	 * Use good old Java synchronization
	 */
	public void testStopUsingVolatile() throws InterruptedException {
		stop = false;
		begin(() -> {
			long tmp = 0;
			while (!volatileStop) {
				tmp++;
			}
			return tmp;
		}, "Volatile stop");
		volatileStop = true;
		report(true);
	}

	/*
	 * Worker
	 */
	private Thread begin(Callable<Long> callable, String title)
			throws InterruptedException {
		finished.set(false);

		System.out.printf("%-30s", title);
		ScheduledExecutorService es = Executors.newScheduledThreadPool(100);
		AtomicReference<Thread> thread = new AtomicReference<Thread>();

		es.execute(() -> {
			try {
				thread.set(Thread.currentThread());
				long count;
				count = callable.call();
				finished.set(true);
				System.out.printf(" %s\n", count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Thread.sleep(3000);
		return thread.get();
	}

	void report(boolean expected) throws InterruptedException {
		Thread.sleep(3000);
		if (!finished.get())
			System.out.printf(" expected=%s never made it\n", expected);
		assertEquals(expected, finished.get());
	}

}
