package net.johnsonlau.jpass;

import net.johnsonlau.jpass.impl.MyPassLog;
import net.johnsonlau.jpass.lib.DnsServer;
import net.johnsonlau.jpass.lib.HttpServer;
import net.johnsonlau.jpass.lib.SshClient;
import net.johnsonlau.jpass.lib.TranServer;
import net.johnsonlau.jpass.lib.conf.PassLog;
import net.johnsonlau.jpass.lib.conf.PassSettings;

public class App {
	public static PassSettings settings;
	public static PassLog log;

	public static void main(String[] args) {
		initMembers();
		SshClient.connect();
		
		Thread httpServerThread = null;
		Thread tranServerThread = null;
		Thread dnsServerThread = null;

		if (settings.getHttpPort() != 0) {
			HttpServer httpServer = new HttpServer();
			httpServerThread = new Thread(httpServer, "httpServer");
			httpServerThread.start(); // run at a new thread
			//thread.run(); //run at the current thread
		}

		if (settings.getTranPort() != 0) {
			TranServer tranServer = new TranServer();
			tranServerThread = new Thread(tranServer, "tranServer");
			tranServerThread.start();
		}

		if (settings.getDnsPort() != 0) {
			DnsServer dnsServer = new DnsServer();
			dnsServerThread = new Thread(dnsServer, "dnsServer");
			dnsServerThread.start();
	    }

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException ex) {
		}

		if (httpServerThread != null) {
			httpServerThread.interrupt();
		}
		if (tranServerThread != null) {
			tranServerThread.interrupt();
		}
		if (dnsServerThread != null) {
			dnsServerThread.interrupt();
		}

		SshClient.disconnect();
	}

	private static void initMembers() {
		log = new MyPassLog();

		settings = new PassSettings();
		settings.setSshServer(System.getProperty("sshServer", ""));
		settings.setSshPort(Integer.parseInt(System.getProperty("sshPort", "22")));
		settings.setSshUsername(System.getProperty("sshUsername", "root"));
		settings.setSshPassword(System.getProperty("sshPassword", ""));
		settings.setHttpPort(Integer.parseInt(System.getProperty("httpPort", "8119")));
		settings.setTranPort(Integer.parseInt(System.getProperty("tranPort", "8117")));
		settings.setDnsPort(Integer.parseInt(System.getProperty("dnsPort", "53")));
		settings.setDnsRemoteServer(System.getProperty("dnsRemoteServer", "127.0.0.1"));
		settings.setDnsRemotePort(Integer.parseInt(System.getProperty("dnsRemotePort", "53")));
		settings.setServeLocalOnly(Boolean.parseBoolean(System.getProperty("serveLocalOnly", "true")));
	}
}
