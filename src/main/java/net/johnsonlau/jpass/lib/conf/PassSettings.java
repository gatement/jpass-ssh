package net.johnsonlau.jpass.lib.conf;

import net.johnsonlau.jpass.lib.ServiceType;

public class PassSettings {
	private int SSH_ALIVE_MAX_COUNT = 3;
	private int SSH_ALIVE_INTERVAL_MS = 60000;
	private int SSH_CHANNEL_OPEN_TIMEOUT_MS = 10000;
	private int LISTENER_SO_TIMEOUT = 1000;
	private int LISTENER_BACKLOG = 50;

	private String serverAddr = "";
	private int serverPort = 22;
	private String username = "root";
	private String password = "";
	private int proxyPort = 8119;
	private boolean serveLocalOnly = true;
	private ServiceType serviceType = ServiceType.HTTP;

	public PassSettings() {
	}

	public PassSettings(String serverAddr, int serverPort, String username, String password, int proxyPort, boolean serveLocalOnly, ServiceType serviceType) {
		this.serverAddr = serverAddr;
		this.serverPort = serverPort;
		this.username = username;
		this.password = password;
		this.proxyPort = proxyPort;
		this.serveLocalOnly = serveLocalOnly;
		this.serviceType = serviceType;
	}

	public String getServerAddr() {
		return this.serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public int getServerPort() {
		return this.serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getProxyPort() {
		return this.proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean getServeLocalOnly() {
		return this.serveLocalOnly;
	}

	public void setServeLocalOnly(boolean serveLocalOnly) {
		this.serveLocalOnly = serveLocalOnly;
	}

	public ServiceType getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public int getSshAliveMaxCount() {
		return SSH_ALIVE_MAX_COUNT;
	}

	public int getSshAliveIntervalMs() {
		return SSH_ALIVE_INTERVAL_MS;
	}

	public int getSshChannelOpenTimeoutMs() {
		return SSH_CHANNEL_OPEN_TIMEOUT_MS;
	}

	public int getListenerSoTimeout() {
		return LISTENER_SO_TIMEOUT;
	}

	public int getListenerBacklog() {
		return LISTENER_BACKLOG;
	}
}
