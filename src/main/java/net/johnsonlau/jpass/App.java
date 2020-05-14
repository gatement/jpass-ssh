package net.johnsonlau.jpass;

import net.johnsonlau.jpass.impl.MyPassLog;
import net.johnsonlau.jpass.lib.HttpServer;
import net.johnsonlau.jpass.lib.SshClient;
import net.johnsonlau.jpass.lib.conf.PassLog;
import net.johnsonlau.jpass.lib.conf.PassSettings;

public class App {
	public static PassSettings settings;
	public static PassLog log;

	public static void main(String[] args) {
		initMembers();
		test();

		SshClient.connect();
		
		Thread httpServerThread = null;

		HttpServer httpServer = new HttpServer();
		httpServerThread = new Thread(httpServer, "httpServer");
		httpServerThread.start(); // run at a new thread
		//thread.run(); //run at the current thread

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException ex) {
		}

		if (httpServerThread != null) {
			httpServerThread.interrupt();
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
		settings.setServeLocalOnly(Boolean.parseBoolean(System.getProperty("serveLocalOnly", "true")));
	}
	
	private static void test() {
		byte a = 23;
		byte b = -1;
		int ia = a & 0xff;
		int ib = b & 0xff;
        //log.info(String.valueOf(ia) + ", " + String.valueOf(ib));
	}
}
