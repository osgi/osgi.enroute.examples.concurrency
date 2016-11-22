package osgi.enroute.examples.concurrency.dynamic;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Demonstrates indexing services by property when there are duplicates in the keys.
 */
@Component(service=HolderIndexedMultipleServices.class)
public class HolderIndexedMultipleServices {

	class Holder {
		final Set<Foo> foos =  ConcurrentHashMap.newKeySet();

		boolean add( Foo foo) {
			return foos.add(foo);
		}

		boolean remove( Foo foo) {
			return foos.remove(foo);
		}
	}

	final ConcurrentMap<String, Holder> multis = new ConcurrentHashMap<>();

	@Reference(cardinality=ReferenceCardinality.MULTIPLE, policy=ReferencePolicy.DYNAMIC)
	void addFoo(Foo foo, Map<String, Object> props) {
		String key = (String) props.get("id");
		if (key == null)
			return;

		multis.compute(key, (k,v) -> {
			if ( v == null)
				v = new Holder();
			System.out.println("Adding Holder " + key + "=" + foo);
			v.add(foo);
			return v;
		});
		
	}

	void removeFoo(Foo foo, Map<String, Object> props) {
		String key = (String) props.get("id");
		if (key == null)
			return;

		
		multis.computeIfPresent(key, (k,v) -> {
			v.remove(foo);
			System.out.println("Removing Holder " + key);
			return v.foos.isEmpty() ? null : v;
		});
	}

}
