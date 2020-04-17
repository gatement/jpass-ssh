package net.johnsonlau.jpass.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.jcraft.jsch.Channel;

import net.johnsonlau.jpass.App;

public class TranSocketHandler extends Thread {

	private Socket socket;

	public TranSocketHandler(Socket socket) {
		this.socket = socket;
		TranServer.connectionCount.incrementAndGet();
	}

	@Override
	public void run() {
		OutputStream clientOutput = null;
		InputStream clientInput = null;
		Socket proxySocket = null;
		InputStream proxyInput = null;
		OutputStream proxyOutput = null;
		Channel sshChannel = null;
		try {
			clientInput = socket.getInputStream();
			clientOutput = socket.getOutputStream();

			// get target host and port
			byte[] header = {0, 0, 0, 0, 0, 0};
			int readBytes = 0;
			while(readBytes < 6) {
				readBytes += clientInput.read(header, 0, 6 - readBytes);
			}
			String targetHost = (header[0] & 0xff) + "." + (header[1] & 0xff) + "." + (header[2] & 0xff) + "." + (header[3] & 0xff);
			int targetPort = (header[4] & 0xff) * 256 + (header[5] & 0xff);

			App.log.info("TRAN[" + Integer.valueOf(TranServer.connectionCount.get()) + "] " + targetHost + ":" + String.valueOf(targetPort));

			// create proxy channel
			// Use SSH Tunnel to connect remote server
			sshChannel = SshClient.getStreamForwarder(targetHost, targetPort, false);
			proxyInput = sshChannel.getInputStream();
			proxyOutput = sshChannel.getOutputStream();
		
			// do the following transmission
			if (sshChannel != null) {
				// New thread continue sending data to target server
				new PassStreamingThread(clientInput, proxyOutput).start();

				// Receive target response
				byte[] data = new byte[65536]; // 64KB
				int readCount = proxyInput.read(data);
				while (readCount != -1) {
					clientOutput.write(data, 0, readCount);
					clientOutput.flush();
					readCount = proxyInput.read(data);
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
			if (proxySocket != null) {
				try {
					proxySocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (clientInput != null) {
				try {
					clientInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (clientOutput != null) {
				try {
					clientOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
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

			TranServer.connectionCount.decrementAndGet();
		}
	}
}
