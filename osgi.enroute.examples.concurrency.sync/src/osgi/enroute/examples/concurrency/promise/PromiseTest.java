package osgi.enroute.examples.concurrency.promise;

import java.math.BigInteger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.osgi.util.promise.Promise;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class PromiseTest extends OSGiTestCase {


	public void testSimple() throws Exception {
		Factorial fac = getService( Factorial.class);
		Promise<BigInteger> p = fac.fac(100_000);
		assertFalse( p.isDone());

		Semaphore s = new Semaphore(0);
		
		p.onResolve( ()-> s.release());
		
		assertTrue( s.tryAcquire(1, TimeUnit.MINUTES));
		assertNull( p.getFailure());
		assertNotNull( p.getValue());
		System.out.println(p.getValue());
	}

}
