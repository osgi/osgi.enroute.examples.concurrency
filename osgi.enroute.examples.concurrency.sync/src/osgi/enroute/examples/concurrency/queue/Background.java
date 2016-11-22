package osgi.enroute.examples.concurrency.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component(service = Background.class)
public class Background extends Thread {

	final BlockingQueue<Runnable>	queue	= new LinkedBlockingQueue<>();
	final AtomicInteger				counter	= new AtomicInteger(0);

	public Background() {
		super("Background");
		setDaemon(true);
	}

	@Activate
	void activate() {
		start();
	}

	@Deactivate
	void deactivate() {
		interrupt();
	}

	public void work(Runnable r) {
		queue.add(r);
	}

	public void run() {
		while (!isInterrupted())
			try {
				Runnable take = queue.take();

				try {
					take.run();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (InterruptedException e) {
				interrupt();
				return;
			} catch (Throwable e) {
				e.printStackTrace();
			}
	}
}
