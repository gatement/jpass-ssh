package net.johnsonlau.jpass;

import net.johnsonlau.jpass.impl.MyPassLog;
import net.johnsonlau.jpass.lib.PassServer;
import net.johnsonlau.jpass.lib.ServiceType;
import net.johnsonlau.jpass.lib.conf.PassSettings;

public class App {
	public static void main(String[] args) {
		PassSettings settings = new PassSettings();
		settings.setServerAddr(System.getProperty("serverAddr", ""));
		settings.setServerPort(Integer.parseInt(System.getProperty("serverPort", "22")));
		settings.setUsername(System.getProperty("username", "root"));
		settings.setPassword(System.getProperty("password", ""));
		settings.setProxyPort(Integer.parseInt(System.getProperty("proxyPort", "8119")));
		settings.setServeLocalOnly(Boolean.parseBoolean(System.getProperty("serveLocalOnly", "true")));
		settings.setServiceType(ServiceType.valueOf(System.getProperty("serviceType", "http").toUpperCase()));

		final Thread thread = new Thread(new PassServer(settings, new MyPassLog()), "ProxyThread");

		// run at the current thread
		thread.run();

		/*
		// run at a new thread
		thread.start();

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException ex) {
		}
		thread.interrupt();
		*/
	}
}
