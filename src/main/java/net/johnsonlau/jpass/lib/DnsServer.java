package net.johnsonlau.jpass.lib;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import net.johnsonlau.jpass.App;

public class DnsServer implements Runnable {
	public static AtomicInteger connectionCount = new AtomicInteger(0);

	public DnsServer() {
	}

	public void run() {
	    DatagramSocket serverSocket = null;
		try {
			InetAddress addr = App.settings.getServeLocalOnly() ? InetAddress.getLoopbackAddress() : null;
			serverSocket = new DatagramSocket(App.settings.getDnsPort(), addr);
			serverSocket.setSoTimeout(App.settings.getListenerSoTimeout());
			App.log.info("==== jPass DNS started at port: " + String.valueOf(App.settings.getDnsPort()));
			while (true) {
				try {
					byte[] buf = new byte[8192];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
	                serverSocket.receive(packet);
	                if (packet.getLength() > 0) {
						new DnsSocketHandler(serverSocket, packet).start();
	                }
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
			App.log.info("==== jPass DNS stopped.");
		}
	}
}
