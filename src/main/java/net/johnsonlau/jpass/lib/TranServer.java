package net.johnsonlau.jpass.lib;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import net.johnsonlau.jpass.App;

public class TranServer implements Runnable {
	public static AtomicInteger connectionCount = new AtomicInteger(0);

	public TranServer() {
	}

	public void run() {
	    ServerSocket serverSocket = null;
		try {
			InetAddress addr = App.settings.getServeLocalOnly() ? InetAddress.getLoopbackAddress() : null;
			serverSocket = new ServerSocket(App.settings.getTranPort(), App.settings.getListenerBacklog(), addr);
			serverSocket.setSoTimeout(App.settings.getListenerSoTimeout());
			App.log.info("==== jPass TRAN started at port: " + String.valueOf(App.settings.getTranPort()));
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					new TranSocketHandler(socket).start();
				} catch (SocketTimeoutException ex) {
				}
				
				Thread.sleep(1); // allow for interrupting
			}
		} catch (InterruptedException ex) {
		} catch (Exception ex) {
			App.log.info("exception: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
				    serverSocket.close();
				} catch (Exception ex) {
         			App.log.info("exception: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
			App.log.info("==== jPass TRAN stopped.");
		}
	}
}
