package osgi.enroute.examples.concurrency.modified_ref;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service=DynamicUpdatedReferenceProperties.class)
public class DynamicUpdatedReferenceProperties {

	int foo;
	
	@Reference
	void setFoo( Foo foo, Map<String,Object> map) {
		this.foo = (int) map.getOrDefault("property", -1);
	}
	
	void updatedFoo( Map<String,Object> map) {
		this.foo = (int) map.getOrDefault("property", -2);
	}
}
