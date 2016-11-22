package osgi.enroute.examples.concurrency.optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(service=ReluctantOptionalReference.class)
public class ReluctantOptionalReference {

	@Reference(cardinality=ReferenceCardinality.OPTIONAL)
	Foo reluctantOptionalReference;

}
