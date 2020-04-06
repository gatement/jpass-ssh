package net.johnsonlau.jpass.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.jcraft.jsch.Channel;

public class PassSocketHandler extends Thread {

	private Socket socket;

	public PassSocketHandler(Socket socket) {
		this.socket = socket;
		PassServer.connectionCount.incrementAndGet();
		//ProxyServer.log.info("Creating connection, connection count up to = " + Integer.valueOf(ProxyServer.connectionCount.get()));
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

			if (PassServer.settings.getServiceType() == ServiceType.HTTP) {
				StringBuilder headStr = new StringBuilder();

				int inputByte = clientInput.read();
				while (inputByte != -1) {
					headStr.append((char) inputByte);

					// Finish receiving HTTP headers
					if (headStr.length() > 4 && headStr.substring(headStr.length() - 4, headStr.length()).equals("\r\n\r\n")) {
						
						PassServer.log.info(headStr.toString());

						// Extract HTTP method and target server:
						//   Example1: CONNECT www.example.com:443 HTTP/1.1
						//   Example2: POST http://www.example.com/a/b/c HTTP/1.1
						//   Example3: GET /johnsontest HTTP/1.1
						//             Host: www.example.com
						//             Host: www.example.com:8080

						String[] headerLines = headStr.toString().split("\r\n");
						String[] firstLine = headerLines[0].split(" ");
						
						// 1. get httpMethod
						String httpMethod = firstLine[0];
						String hostLine = firstLine[1];

						// 2. get targetHost, targetPort
						String targetHost = "";
						int targetPort = 80;
						if ("CONNECT".equalsIgnoreCase(httpMethod)) {
							String[] host = hostLine.split(":");
							targetHost = host[0];
							if (host.length > 1) {
								targetPort = Integer.valueOf(host[1]);
							}
						} else if (hostLine.toLowerCase().startsWith("http")) {
							String[] host = hostLine.split("://")[1].split("/")[0].split(":");
							targetHost = host[0];
							if (host.length > 1) {
								targetPort = Integer.valueOf(host[1]);
							}
						} else {
							PassServer.log.info("Error: could not resolve target host and port!");
						}

						// Connect target server
						PassServer.log.info("HTTP[" + Integer.valueOf(PassServer.connectionCount.get()) + "] " + targetHost + ":" + String.valueOf(targetPort));

						// 3. create proxy channel
						// Use SSH Tunnel to connect remote server
						sshChannel = SshClient.getStreamForwarder(targetHost, targetPort, false);
						proxyInput = sshChannel.getInputStream();
						proxyOutput = sshChannel.getOutputStream();
						// Optional: Connect remote server directly
						// proxySocket = new Socket(targetHost, targetPort);
						// proxyInput = proxySocket.getInputStream();
						// proxyOutput = proxySocket.getOutputStream();

						// 4. response CONNECT or transmit to targetHost
						// Process HTTP Method CONNECT
						if ("CONNECT".equalsIgnoreCase(httpMethod)) {
							// For HTTPS request, consume the initiative HTTP request and send back response
							clientOutput.write("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
							clientOutput.flush();
						} else {
							// For HTTP request, transmit the initiative HTTP request
							proxyOutput.write(headStr.toString().getBytes());
							proxyOutput.flush();
						}

						break;
					}

					inputByte = clientInput.read();
				}
			} else if (PassServer.settings.getServiceType() == ServiceType.TRANSPARENT) {
				byte[] header = {0, 0, 0, 0, 0, 0};
				int readBytes = 0;
				while(readBytes < 6) {
					readBytes += clientInput.read(header, 0, 6 - readBytes);
				}

				String targetHost = (header[0] & 0xff) + "." + (header[1] & 0xff) + "." + (header[2] & 0xff) + "." + (header[3] & 0xff);
				int targetPort = (header[4] & 0xff) * 256 + (header[5] & 0xff);

				// Connect target server
				PassServer.log.info("TRAN[" + Integer.valueOf(PassServer.connectionCount.get()) + "] " + targetHost + ":" + String.valueOf(targetPort));

				// 3. create proxy channel
				// Use SSH Tunnel to connect remote server
				sshChannel = SshClient.getStreamForwarder(targetHost, targetPort, false);
				proxyInput = sshChannel.getInputStream();
				proxyOutput = sshChannel.getOutputStream();
			} else {
				PassServer.log.info("Unsupported ServiceType.");
			}
		
			// 5. do the following transmission
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
			PassServer.log.info("exception: " + ex.getMessage());
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

			PassServer.connectionCount.decrementAndGet();
			//ProxyServer.log.info("Closed connection, connection count down to = " + Integer.valueOf(ProxyServer.connectionCount.get()));
		}
	}
}
