package net.johnsonlau.jproxy;

import net.johnsonlau.jproxy.conf.MyProxyLog;
import net.johnsonlau.jproxy.conf.MyProxySettings;
import net.johnsonlau.jproxy.lib.ProxyMain;

public class App {
	public static void main(String[] args) {
		new ProxyMain(new MyProxySettings(), new MyProxyLog()).run();
	}
}
