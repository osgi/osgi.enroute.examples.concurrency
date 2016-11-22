package osgi.enroute.examples.concurrency.cycle;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service=Top.class,immediate=false)
public class Top {

	@Reference
	volatile List<Bottom>		circularReferenceToBottoms;
	
}
