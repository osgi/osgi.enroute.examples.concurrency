package osgi.enroute.examples.concurrency.log;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;

@Component
public class LogViewer {

	final LogListener listener = this::log;
	
	@Reference
	void addLogReader(LogReaderService logreader) {
		logreader.addLogListener( listener );
	}
	void removeLogReader(LogReaderService logreader) {
		logreader.removeLogListener( listener );
	}
	
	void log( LogEntry entry ) {
		String sname = "";
		if ( entry.getServiceReference() != null) {
			sname = (String) entry.getServiceReference().getProperty("component.name");
			if (sname == null) {
				sname = (String) entry.getServiceReference().getProperty("service.pid");
			}
			if (sname == null) {
				sname = ""+ entry.getServiceReference().getProperty("service.id");
			}
		}
		System.out.printf("%tT %s %s %s\n", entry.getTime(), entry.getMessage(), entry.getBundle().getSymbolicName(), sname);
	}
}
