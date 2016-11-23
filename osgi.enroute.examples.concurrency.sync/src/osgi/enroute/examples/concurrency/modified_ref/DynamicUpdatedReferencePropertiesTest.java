package osgi.enroute.examples.concurrency.modified_ref;

import java.util.Hashtable;

import org.osgi.framework.ServiceRegistration;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class DynamicUpdatedReferencePropertiesTest extends OSGiTestCase {

	public void testDynamicChangedServiceProperties() throws Exception {
		Foo foo = new Foo() {

		};
		Hashtable<String, Object> serviceProperties = new Hashtable<>();
		serviceProperties.put("property", 1);

		ServiceRegistration<Foo> r = getContext().registerService(Foo.class, foo,
				serviceProperties);
		try {
			DynamicUpdatedReferenceProperties x = getService(
					DynamicUpdatedReferenceProperties.class);
			assertEquals(1, x.foo);

			serviceProperties.put("property", 2);
			r.setProperties(serviceProperties);

			DynamicUpdatedReferenceProperties x2 = getService(
					DynamicUpdatedReferenceProperties.class);
			sleep(100);
			assertEquals( x, x2);
			assertEquals(2, x2.foo);
		} finally {
			r.unregister();
		}
	}

}
