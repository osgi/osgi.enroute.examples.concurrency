package osgi.enroute.examples.concurrency.events;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = EventLoop.class)
public class EventLoop {

	@Reference
	volatile List<EventHandler>		handlers;

	final Map<EventHandler, Long>	blacklist	= Collections
			.synchronizedMap(new WeakHashMap<>());

	@Reference
	Executor						executor;

	public void dispatch(int n) {
		handlers.stream() //
				.filter(this::timeout)
				.forEach((handler) -> dispatchInBackground(n, handler));
	}

	private void dispatchInBackground(int n, EventHandler handler) {
		executor.execute(() -> {
			try {
				handler.send(n);
			} catch (Exception e) {
				blacklist.put(handler, System.currentTimeMillis() + 5000);
				e.printStackTrace();
			}
		});
	}

	boolean timeout(EventHandler handler) {
		long currentTimeMillis = System.currentTimeMillis();
		Long timeout = blacklist.get(handler);

		if (timeout != null) {

			if (timeout > currentTimeMillis)
				return false;

			blacklist.remove(handler);
		}
		return true;
	}
}
