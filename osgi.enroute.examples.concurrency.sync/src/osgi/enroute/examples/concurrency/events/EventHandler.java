package osgi.enroute.examples.concurrency.events;

public interface EventHandler {

	void send(int n) throws Exception;

}
