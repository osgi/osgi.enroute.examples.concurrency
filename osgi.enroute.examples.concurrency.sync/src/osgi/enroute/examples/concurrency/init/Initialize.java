package osgi.enroute.examples.concurrency.init;

import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.promise.Deferred;

@Component
public class Initialize implements Work {
	final Deferred<Delegate>	deferred	= new Deferred<>();
	volatile boolean closed;
	
	@Reference
	Executor					executor;

	@Activate
	void activate() throws Exception {
		executor.execute(() -> {
			try {
				Thread.sleep(2000); // long running init
				deferred.resolve(new Delegate());
			} catch (Exception e) {
				deferred.fail(e);
			}
		});
	}

	@Override
	public void work() throws Exception {
		deferred.getPromise().getValue().work();
	}

	@Deactivate
	void deactivate() {
		deferred.getPromise().onResolve(this::close);
	}

	void close() {
		closed = true;
	}

}
