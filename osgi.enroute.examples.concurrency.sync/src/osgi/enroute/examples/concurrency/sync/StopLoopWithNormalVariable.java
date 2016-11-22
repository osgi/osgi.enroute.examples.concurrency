package osgi.enroute.examples.concurrency.sync;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * This is an example belonging to
 * http://enroute.osgi.org/appnotes/concurrency.html
 * <p>
 * This class shows a loop that is started in the activate and that is
 * <em>attempted</em> to be stopped in the deactivate method. However, because
 * there is no before/after relationship between the background threads read of
 * {@code stop} and the {@code deactivate} method's write of it, the background
 * thread never sees the change and will therefore never stop.
 * <p>
 * You can try out this example by starting and stopping this bundle.
 * 
 */

@Component(service=StopLoopWithNormalVariable.class)
public class StopLoopWithNormalVariable extends Thread {

	boolean stop;

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
