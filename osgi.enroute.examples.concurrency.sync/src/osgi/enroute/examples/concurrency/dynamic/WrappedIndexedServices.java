package osgi.enroute.examples.concurrency.dynamic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Demonstrating wrapping indexing a service and wrapping it.
 */
@Component(service=WrappedIndexedServices.class)
public class WrappedIndexedServices {

	class Wrap {
		final Foo foo;
		boolean closed;
		
		public Wrap(Foo foo) {
			this.foo = foo;
		}

		public void close() {
			closed = true;
		}
	}

	final ConcurrentMap<String, Wrap> wrappedServices = new ConcurrentHashMap<>();

	@Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC)
	void addFoo(Foo foo, Map<String, Object> props) {
		String key = (String) props.get("id");
		if (key == null)
			return;

		Wrap previous = wrappedServices.put(key, new Wrap(foo));
		if ( previous != null) {
			previous.close();
		}
		
	}

	void removeFoo(Map<String, Object> props) {
		String key = (String) props.get("id");
		if (key == null)
			return;

		Wrap previous = wrappedServices.remove(key);
		if ( previous != null) {
			previous.close();
		}
	}

}
