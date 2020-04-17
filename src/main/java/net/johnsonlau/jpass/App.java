package net.johnsonlau.jpass;

import net.johnsonlau.jpass.impl.MyPassLog;
import net.johnsonlau.jpass.lib.HttpServer;
import net.johnsonlau.jpass.lib.ServiceType;
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

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException ex) {
		}
		httpServerThread.interrupt();
		tranServerThread.interrupt();

		SshClient.disconnect();
	}

	private static void initMembers() {
		log = new MyPassLog();

		settings = new PassSettings();
		settings.setServerAddr(System.getProperty("serverAddr", ""));
		settings.setServerPort(Integer.parseInt(System.getProperty("serverPort", "22")));
		settings.setUsername(System.getProperty("username", "root"));
		settings.setPassword(System.getProperty("password", ""));
		settings.setHttpPort(Integer.parseInt(System.getProperty("httpPort", "8119")));
		settings.setTranPort(Integer.parseInt(System.getProperty("tranPort", "8117")));
		settings.setDnsPort(Integer.parseInt(System.getProperty("dnsPort", "53")));
		settings.setServeLocalOnly(Boolean.parseBoolean(System.getProperty("serveLocalOnly", "true")));
	}
}
