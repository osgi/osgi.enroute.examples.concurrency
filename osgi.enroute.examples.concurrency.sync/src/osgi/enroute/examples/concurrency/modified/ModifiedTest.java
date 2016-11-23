package osgi.enroute.examples.concurrency.modified;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class ModifiedTest extends OSGiTestCase {

	public void testStaticModifiedConfiguration() throws Exception {
		ConfigurationAdmin cm = getService(ConfigurationAdmin.class);
		Configuration config = cm.getConfiguration(StaticModifiedConfiguration.class.getName(), "?");
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("config", 2);
		config.update(properties);
		
		StaticModifiedConfiguration sm = getService( StaticModifiedConfiguration.class);
		assertNotNull(sm);
		assertEquals(2, sm.config);
		
		properties.put("config", 3);
		config.update(properties);
		
		sleep(1000);
		StaticModifiedConfiguration sm2 = getService( StaticModifiedConfiguration.class);
		assertTrue( sm != sm2);
		assertEquals(3, sm2.config);
	}

	public void testDynamicModifiedConfiguration() throws Exception {
		ConfigurationAdmin cm = getService(ConfigurationAdmin.class);
		Configuration config = cm.getConfiguration(DynamicModifiedConfiguration.class.getName(), "?");
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("config", 2);
		config.update(properties);
		
		DynamicModifiedConfiguration dm = getService( DynamicModifiedConfiguration.class);
		assertNotNull(dm);
		assertEquals(2, dm.config);
		
		
		properties.put("config", 3);
		config.update(properties);
		
		sleep(1000);
		DynamicModifiedConfiguration dm2 = getService( DynamicModifiedConfiguration.class);
		assertTrue( dm == dm2);
		assertEquals(3, dm2.config);
	}
}
