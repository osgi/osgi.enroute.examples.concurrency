package osgi.enroute.examples.concurrency.sync;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * This is an example belonging to
 * http://enroute.osgi.org/appnotes/concurrency.html
 * <p>
 * This class shows a loop that is started in the activate and that is stopped
 * in the deactivate method. To create a before/after relationship between the
 * background threads read of {@code stop} and the {@code deactivate} method's
 * write of it a volatile variable is used.
 * <p>
 * You can try out this example by starting and stopping this bundle.
 * 
 */
@Component(service=StopLoopWithVolatileVariable.class)
public class StopLoopWithVolatileVariable extends Thread {

	volatile boolean stop;

	@Activate
	void activate() {
		start();
	}

	public void run() {
		while (!stop)
			;
	}

	@Deactivate
	void deactivate() {
		stop = true;
	}
}
