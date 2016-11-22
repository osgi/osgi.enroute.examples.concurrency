package osgi.enroute.examples.concurrency.cycle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service=Bottom.class, immediate=true)
public class Bottom {

	@Reference
	Top			circularReferenceToTop;
	
}
