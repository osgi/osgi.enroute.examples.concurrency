package osgi.enroute.examples.concurrency.init;

import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;

@Component
public class Initialize implements Work {
	Promise<Delegate>	promise;
	volatile boolean	closed;

	@Reference
	Executor			executor;

	@Activate
	void activate() throws Exception {
		Deferred<Delegate> deferred = new Deferred<>();
		executor.execute(() -> {
			try {
				Thread.sleep(2000); // long running init
				deferred.resolve(new Delegate());
			} catch (Exception e) {
				deferred.fail(e);
			}
		});
		promise = deferred.getPromise();
	}

	@Override
	public void work() throws Exception {
		promise.getValue().work();
	}

	@Deactivate
	void deactivate() {
		promise.onResolve(this::close);
	}

	void close() {
		closed = true;
	}

}
