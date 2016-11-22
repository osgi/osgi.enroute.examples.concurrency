package osgi.enroute.examples.concurrency.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(immediate=true)
public class BackgroundIO extends Thread {

	ServerSocket	serverSocket;

	@Reference
	Executor		executor;

	public BackgroundIO() {
		super("BackgroundIO");
	}

	@Activate
	void startBackgroundThread() {
		start();
	}

	@Deactivate
	void stopBackgroundThread() throws IOException {
		interrupt();
		bestEffortClose(serverSocket);
	}

	public void run() {
		
		List<Socket> openConnections = new CopyOnWriteArrayList<>();

		try {
			while (!isInterrupted())
				try {
					serverSocket = new ServerSocket(0);
					while (!isInterrupted()) {
						Socket connection = serverSocket.accept();
						try {
							openConnections.add(connection);
							processConnection(connection,openConnections);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception t) {

					// No report of exceptions
					// when we're interrupted
					
					if (isInterrupted())
						return;

					t.printStackTrace();

					try {
						// Prevent overload (e.g. bind exception
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						interrupt();
						return;
					}
				}
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally {
			bestEffortClose(serverSocket);
			openConnections.forEach( this::bestEffortClose );
		}
	}

	private void bestEffortClose(AutoCloseable s) {
		try {
			if ( s == null)
				return;
			s.close();
		} catch( Exception e) {
			//ignore
		}
	}

	private void processConnection(Socket connection, List<Socket> openConnections) {
		executor.execute(() -> {
			try {
				while(connection.isConnected()) {
					// do work
				}
			} finally {
				openConnections.remove(connection);
				bestEffortClose(connection);
			}
		});
	}
}
