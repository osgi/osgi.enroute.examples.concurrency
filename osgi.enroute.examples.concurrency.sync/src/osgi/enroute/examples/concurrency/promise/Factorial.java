package osgi.enroute.examples.concurrency.promise;

import java.math.BigInteger;
import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;

@Component(service = Factorial.class)
public class Factorial {

	@Reference
	Executor executor;

	public Promise<BigInteger> fac(long n) {
		Deferred<BigInteger> deferred = new Deferred<>();
		executor.execute(() -> {
			try {
				
				BigInteger result = BigInteger.ONE;
				for ( int i= 2; i<n; i++) {
					result = result.multiply( BigInteger.valueOf(i));
				}
				deferred.resolve(result);
			} catch (Throwable e) {
				e.printStackTrace();
				deferred.fail(e);
			}
		});
		return deferred.getPromise();
	}

}
