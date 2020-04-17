package net.johnsonlau.jpass.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import com.jcraft.jsch.Channel;

import net.johnsonlau.jpass.App;

public class DnsSocketHandler extends Thread {

	private DatagramPacket packet;

	public DnsSocketHandler(DatagramPacket packet) {
		this.packet = packet;
		DnsServer.connectionCount.incrementAndGet();
	}

	@Override
	public void run() {
		InputStream proxyInput = null;
		OutputStream proxyOutput = null;
		Channel sshChannel = null;
		DatagramSocket client = null;
		try {
			String fromHost = packet.getAddress().getHostAddress();
			int fromPort = packet.getPort();

			// Connect target server
			App.log.info("DNS[" + Integer.valueOf(DnsServer.connectionCount.get()) + "] " + fromHost + ":" + String.valueOf(fromPort));

			// create proxy channel
			// Use SSH Tunnel to connect remote server
			sshChannel = SshClient.getStreamForwarder(App.settings.getDnsRemoteServer(), App.settings.getDnsRemotePort(), false);
			proxyInput = sshChannel.getInputStream();
			proxyOutput = sshChannel.getOutputStream();
		
			// do the following transmission
			if (sshChannel != null) {
				// receive target response
				byte[] data = new byte[65536]; // 64KB
				int readCount = 0;
				while (true) {
					int c = proxyInput.read(data, readCount, data.length - readCount);
					if (c != -1) {
						readCount += c;
					} else {
						break;
					}
				}
				
				// send to client
	            client = new DatagramSocket();
				DatagramPacket packets = new DatagramPacket(data, readCount, new InetSocketAddress(fromHost, fromPort));
				client.send(packets);
			}
		} catch (SocketException ex) {
			// peer closed the socket
		} catch (Exception ex) {
			App.log.info("exception: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (proxyInput != null) {
				try {
					proxyOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (proxyOutput != null) {
				try {
					proxyOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (sshChannel != null) {
				try {
					sshChannel.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			DnsServer.connectionCount.decrementAndGet();
		}
	}
}