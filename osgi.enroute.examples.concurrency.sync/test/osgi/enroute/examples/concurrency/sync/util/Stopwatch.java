package osgi.enroute.examples.concurrency.sync.util;

public class Stopwatch {
	long 	start;
	long 	end;
	
	Stopwatch start() {
		start = end = System.currentTimeMillis();
		return this;
	}
	long stop() {
		end = System.currentTimeMillis();
		return end-start; 
	}
	
	long leap() {
		long end = System.currentTimeMillis();
		return end-start; 
	}
	
	public String toString() {
		return start == end ? "leap " + (leap() / 1000D) + " s" : "stopped " + (end-start)/1000D + " s";  
	}
}
