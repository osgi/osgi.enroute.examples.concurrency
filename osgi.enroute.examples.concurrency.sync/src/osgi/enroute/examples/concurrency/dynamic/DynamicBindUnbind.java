package osgi.enroute.examples.concurrency.dynamic;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = DynamicBindUnbind.class)
public class DynamicBindUnbind {

	final List<Foo> foos = new CopyOnWriteArrayList<>();

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void addFoo(Foo foo) {
		foos.add(foo);
	}

	void removeFoo(Foo foo) {
		foos.remove(foo);
	}
}
