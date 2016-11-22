package osgi.enroute.examples.concurrency.optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(service=DynamicOptionalReference.class)
public class DynamicOptionalReference {

	@Reference(cardinality=ReferenceCardinality.OPTIONAL)
	volatile Foo dynamicOptionalReference;
}
