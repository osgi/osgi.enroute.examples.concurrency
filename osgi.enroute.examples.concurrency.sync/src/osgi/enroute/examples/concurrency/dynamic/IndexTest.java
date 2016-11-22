package osgi.enroute.examples.concurrency.dynamic;

import java.io.Closeable;

import osgi.enroute.examples.concurrency.dynamic.WrappedIndexedServices.Wrap;
import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class IndexTest extends OSGiTestCase {

	public void testPlain() throws Exception {
		PlainIndexedServices plain = getService(PlainIndexedServices.class);
		assertEquals(1, plain.plainServices.size());
		assertTrue(plain.plainServices.containsKey("test"));
		
		try (Closeable newkey = register(Foo.class, new Foo() {
		}, "id", "newkey");) {
			sleep(100);
			assertEquals(2, plain.plainServices.size());
			assertTrue(plain.plainServices.containsKey("newkey"));
		}
		
		assertEquals(1, plain.plainServices.size());
	}

	public void testWrap() throws Exception {
		WrappedIndexedServices wrap = getService(WrappedIndexedServices.class);
		assertEquals(1, wrap.wrappedServices.size());
		assertTrue(wrap.wrappedServices.containsKey("test"));
		
		Wrap newkeyWrap;
		try (Closeable newkey = register(Foo.class, new Foo() {
		}, "id", "newkey");) {
			sleep(100);
			assertEquals(2, wrap.wrappedServices.size());
			assertTrue(wrap.wrappedServices.containsKey("newkey"));
			
			newkeyWrap = wrap.wrappedServices.get("newkey");
			assertFalse( newkeyWrap.closed);
		}
		
		sleep(100);

		assertTrue( newkeyWrap.closed);
	}

	public void testHolder() throws Exception {
		HolderIndexedMultipleServices holder = getService(HolderIndexedMultipleServices.class);
		assertEquals(1, holder.multis.size());
		assertTrue(holder.multis.containsKey("test"));		
		assertEquals(1,holder.multis.get("test").foos.size());
		
		try (Closeable duplicate = register(Foo.class, new Foo() {
		}, "id", "test");) {
			sleep(100);
			assertEquals(1, holder.multis.size());
			assertEquals(2,holder.multis.get("test").foos.size());
		}
		sleep(100);
		assertEquals(1, holder.multis.size());
		assertEquals(1,holder.multis.get("test").foos.size());
	}
}
