package osgi.enroute.examples.concurrency.optional;

import java.io.Closeable;

import osgi.enroute.examples.concurrency.test.OSGiTestCase;

public class OptionalTest extends OSGiTestCase {

	
	public void testOptionalReference() throws Exception   {
		
		DynamicOptionalReference dynamic = getService(DynamicOptionalReference.class);
		GreedyOptionalReference greedy = getService(GreedyOptionalReference.class);
		ReluctantOptionalReference reluct = getService(ReluctantOptionalReference.class);
		
		assertNull( reluct.reluctantOptionalReference);
		assertNull( greedy.greedyOptionalReference);
		assertNull( dynamic.dynamicOptionalReference);
		
		try( Closeable c = register( Foo.class, new Foo() {})) {
			
			sleep(100);
			
			DynamicOptionalReference dynamic2 = getService(DynamicOptionalReference.class);
			GreedyOptionalReference greedy2 = getService(GreedyOptionalReference.class);
			ReluctantOptionalReference reluct2 = getService(ReluctantOptionalReference.class);

			assertEquals( dynamic, dynamic2);
			assertEquals( reluct, reluct2);
			assertFalse( greedy.equals(greedy2));
			
			assertNull( reluct2.reluctantOptionalReference);
			assertNotNull( greedy2.greedyOptionalReference);
			assertNotNull( dynamic2.dynamicOptionalReference);
		}
		sleep(100);
		
		
		DynamicOptionalReference dynamic3 = getService(DynamicOptionalReference.class);
		GreedyOptionalReference greedy3 = getService(GreedyOptionalReference.class);
		ReluctantOptionalReference reluct3 = getService(ReluctantOptionalReference.class);

		assertEquals( dynamic, dynamic3);
		assertEquals( reluct, reluct3);
		assertFalse( greedy.equals(greedy3));
		
		assertNull( reluct.reluctantOptionalReference);
		assertNull( greedy.greedyOptionalReference);
		assertNull( dynamic.dynamicOptionalReference);
	}

}
