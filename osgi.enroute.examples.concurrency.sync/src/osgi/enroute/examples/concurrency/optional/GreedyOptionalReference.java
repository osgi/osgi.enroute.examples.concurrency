package osgi.enroute.examples.concurrency.optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Component(service=GreedyOptionalReference.class)
public class GreedyOptionalReference {

	@Reference(cardinality=ReferenceCardinality.OPTIONAL, policyOption=ReferencePolicyOption.GREEDY)
	Foo greedyOptionalReference;
}
