package osgi.enroute.examples.concurrency.test;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import junit.framework.TestCase;

/**
 * Utility to ease the burden on writing test cases.
 */
public abstract class OSGiTestCase extends TestCase {
	final BundleContext					context		= FrameworkUtil
			.getBundle(getClass()).getBundleContext();
	final List<ServiceTracker<?, ?>>	trackers	= new ArrayList<>();

	@Override
	public void tearDown() {
		ungetAll();
	}

	public void ungetAll() {
		trackers.stream().forEach(ServiceTracker::close);
		trackers.clear();
	}

	public <T> T getService(Class<T> c) throws Exception {
		ServiceTracker<T, T> tracker = new ServiceTracker<T, T>(context, c,
				null);
		tracker.open();
		trackers.add(tracker);
		return tracker.waitForService(10000);
	}

	public <T> Closeable register(Class<T> class1, T foo, Object... args) {
		Hashtable<String, Object> properties = new Hashtable<>();
		for (int i = 0; i < args.length; i += 2) {
			properties.put(args[i].toString(), args[i + 1]);
		}
		ServiceRegistration<T> r = context.registerService(class1, foo,
				properties);
		return () -> r.unregister();
	}

	public void sleep(int ms) throws InterruptedException {
		Thread.sleep(ms);
	}

	public boolean waitFor(BooleanSupplier predicate, int ms)
			throws InterruptedException {
		long deadline = System.currentTimeMillis() + ms;
		while (!predicate.getAsBoolean()) {
			if (deadline < System.currentTimeMillis())
				return false;

			sleep(100);
		}
		return true;
	}

	public void assertWait(BooleanSupplier predicate, int ms)
			throws InterruptedException {
		assertWait(predicate, ms, "");
	}

	public void assertWait(BooleanSupplier predicate, int ms, String message)
			throws InterruptedException {
		long deadline = System.currentTimeMillis() + ms;
		while (!predicate.getAsBoolean()) {
			if (deadline < System.currentTimeMillis())
				fail("Timed out : " + message);

			sleep(100);
		}
	}
}
