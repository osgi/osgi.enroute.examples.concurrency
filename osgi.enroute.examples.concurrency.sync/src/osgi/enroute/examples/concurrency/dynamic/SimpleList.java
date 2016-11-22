package osgi.enroute.examples.concurrency.dynamic;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service=SimpleList.class)
public class SimpleList {

	@Reference
	volatile List<Foo>		dynamicFoos;
}
