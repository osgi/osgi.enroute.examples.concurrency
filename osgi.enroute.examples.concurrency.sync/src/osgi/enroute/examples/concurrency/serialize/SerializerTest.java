package osgi.enroute.examples.concurrency.serialize;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class SerializerTest extends OSGiTestCase {

	volatile int	inside	= -1;


	public void testSerializer() throws Exception {
		Serializer		serializer = getService(Serializer.class);
		ExecutorService pool = Executors.newFixedThreadPool(10);
		AtomicBoolean contention = new AtomicBoolean(false);
		
		try {
			for (int i = 0; i < 10; i++) {
				int activeThread = i;
				pool.execute(() -> {
					serializer.serialize(() -> {
						if ( inside >= 0)
							contention.set(true);
						
						inside = activeThread;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						inside = -1;
					});
				});
			}
		} finally {
			pool.shutdown();
		}
		
		assertFalse( contention.get());
	}
}
