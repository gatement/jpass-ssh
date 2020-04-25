package net.johnsonlau.jpass.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;

import com.jcraft.jsch.Channel;

import net.johnsonlau.jpass.App;

public class DnsSocketHandler extends Thread {

	private DatagramSocket serverSocket;
	private DatagramPacket packet;

	public DnsSocketHandler(DatagramSocket serverSocket, DatagramPacket packet) {
		this.serverSocket = serverSocket;
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
			App.log.info("DNS[" + Integer.valueOf(DnsServer.connectionCount.get()) + "] UDP " + fromHost + ":" + String.valueOf(fromPort) + " -> Remote TCP "  + App.settings.getDnsRemoteServer() + ":" + String.valueOf(App.settings.getDnsRemotePort()));

			// create proxy channel
			// Use SSH Tunnel to connect remote server
			sshChannel = SshClient.getStreamForwarder(App.settings.getDnsRemoteServer(), App.settings.getDnsRemotePort(), false);
			proxyInput = sshChannel.getInputStream();
			proxyOutput = sshChannel.getOutputStream();
		
			// do the following transmission
			if (sshChannel != null) {
				// send request
				byte[] requestData = buildRequestData(packet.getData(), packet.getLength());
				proxyOutput.write(requestData, 0, requestData.length);
				proxyOutput.flush();

				// debug
				//App.log.info("sending: " + String.valueOf(requestData.length));
				//Util.printBytes(requestData, requestData.length);
				
				// receive response
				byte[] data = new byte[65536]; // 64KB
				int dataLen = 0;
				int readCount = 0;
				while (true) {
					int c = proxyInput.read(data, readCount, data.length - readCount);
					if (c != -1) {
						readCount += c;
					}
					if (dataLen == 0 && readCount >= 2) {
						int b = data[0] & 0xff;
						int s = data[1] & 0xff;
						dataLen = b * 256 + s;
					}
					if (readCount >= dataLen + 2) {
						break;
					}
				}

				// debug
				//App.log.info("recvd: " + String.valueOf(readCount));
				//Util.printBytes(data, readCount);

				if (true) {
					// transfer response
					DatagramPacket packets = new DatagramPacket(data, 2, readCount, new InetSocketAddress(fromHost, fromPort));
					serverSocket.send(packets);
				}
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

	private 	byte[] buildRequestData(byte[] payload, int len) {
		int totalLen = len;
		byte[] header = new byte[2];
		header[0] = (byte)(totalLen >> 8);
		header[1] = (byte)(totalLen);
		return Util.mergeBytes(header, Arrays.copyOf(packet.getData(), len));
	}
}
