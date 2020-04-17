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
	private int httpPort = 8119;
	private int tranPort = 8117;
	private int dnsPort = 53;
	private boolean serveLocalOnly = true;

	public PassSettings() {
	}

	public PassSettings(String serverAddr, int serverPort, String username, String password, int proxyPort, boolean serveLocalOnly, ServiceType serviceType) {
		this.serverAddr = serverAddr;
		this.serverPort = serverPort;
		this.username = username;
		this.password = password;
		this.httpPort = proxyPort;
		this.serveLocalOnly = serveLocalOnly;
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

	public int getHttpPort() {
		return this.httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public int getTranPort() {
		return this.tranPort;
	}

	public void setTranPort(int tranPort) {
		this.tranPort = tranPort;
	}

	public int getDnsPort() {
		return this.dnsPort;
	}

	public void setDnsPort(int dnsPort) {
		this.dnsPort = dnsPort;
	}

	public boolean getServeLocalOnly() {
		return this.serveLocalOnly;
	}

	public void setServeLocalOnly(boolean serveLocalOnly) {
		this.serveLocalOnly = serveLocalOnly;
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
