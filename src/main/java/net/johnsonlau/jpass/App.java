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

		HttpServer httpServer = new HttpServer();
		final Thread httpServerThread = new Thread(httpServer, "httpServer");
		httpServerThread.start(); // run at a new thread
		//thread.run(); //run at the current thread

		TranServer tranServer = new TranServer();
		final Thread tranServerThread = new Thread(tranServer, "tranServer");
		tranServerThread.start();

		DnsServer dnsServer = new DnsServer();
		final Thread dnsServerThread = new Thread(dnsServer, "dnsServer");
		dnsServerThread.start();

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException ex) {
		}
		httpServerThread.interrupt();
		tranServerThread.interrupt();
		dnsServerThread.interrupt();

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
