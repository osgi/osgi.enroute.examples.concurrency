package osgi.enroute.examples.concurrency.serialize;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(service=Serializer.class)
public class Serializer {


	List<Runnable> serialized = null;
	final Object lock = new Object();
	
	public void serialize(Runnable runnable) {
	
		synchronized(lock) {
			if ( serialized != null ) {
				serialized.add(runnable);
				return;
			}
			serialized= new ArrayList<>();
		}
		
		while(true) {
		
			try {
				runnable.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			synchronized(lock) {
				if ( serialized.isEmpty() ) {
					serialized = null;
					return;
				}
				runnable = serialized.remove(0);
			}
		}
	}
}
