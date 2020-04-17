package net.johnsonlau.jpass.lib.conf;

public class PassSettings {
	private int SSH_ALIVE_MAX_COUNT = 3;
	private int SSH_ALIVE_INTERVAL_MS = 60000;
	private int SSH_CHANNEL_OPEN_TIMEOUT_MS = 10000;
	private int LISTENER_SO_TIMEOUT = 1000;
	private int LISTENER_BACKLOG = 50;

	private String sshServer = "";
	private int sshPort = 22;
	private String sshUsername = "root";
	private String sshPassword = "";
	private int httpPort = 8119;
	private int tranPort = 8117;
	private int dnsPort = 53;
	private String dnsRemoteServer = "127.0.0.1";
	private int dnsRemotePort = 53;
	private boolean serveLocalOnly = true;

	public PassSettings() {
	}

	public String getSshServer() {
		return this.sshServer;
	}

	public void setSshServer(String sshServer) {
		this.sshServer = sshServer;
	}

	public int getSshPort() {
		return this.sshPort;
	}

	public void setSshPort(int sshPort) {
		this.sshPort = sshPort;
	}

	public String getSshUsername() {
		return this.sshUsername;
	}

	public void setSshUsername(String sshUsername) {
		this.sshUsername = sshUsername;
	}

	public String getSshPassword() {
		return this.sshPassword;
	}

	public void setSshPassword(String sshPassword) {
		this.sshPassword = sshPassword;
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

	public String getDnsRemoteServer() {
		return this.dnsRemoteServer;
	}

	public void setDnsRemoteServer(String dnsRemoteServer) {
		this.dnsRemoteServer = dnsRemoteServer;
	}

	public int getDnsRemotePort() {
		return this.dnsRemotePort;
	}

	public void setDnsRemotePort(int dnsRemotePort) {
		this.dnsRemotePort = dnsRemotePort;
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
