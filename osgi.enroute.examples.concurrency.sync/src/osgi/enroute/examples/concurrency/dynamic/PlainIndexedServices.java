package osgi.enroute.examples.concurrency.dynamic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Demonstrates indexing a service by a property assuming no duplicates
 *
 */
@Component(service=PlainIndexedServices.class, name="plain")
public class PlainIndexedServices {

	final ConcurrentMap<String, Foo> plainServices = new ConcurrentHashMap<>();

	@Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC)
	void addFoo(Foo foo, Map<String, Object> props) {
		String key = (String) props.get("id");
		if (key == null)
			return;

		System.out.println("adding " + key + " " + foo);
		plainServices.put(key, foo);
	}

	void removeFoo(Map<String, Object> props) {
		String key = (String) props.get("id");
		if (key == null)
			return;
		System.out.println("removing " + key );
		plainServices.remove(key);
	}
}
