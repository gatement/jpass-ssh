package net.johnsonlau.jpass.lib;

import java.io.IOException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import net.johnsonlau.jpass.App;

public class SshClient {
    public static Session sshSession;

    public static void connect() {
        try {
            disconnect();

            JSch sshClient = new JSch();
            sshSession = sshClient.getSession(
                    App.settings.getSshUsername(), 
                    App.settings.getSshServer(),
                    App.settings.getSshPort());
            sshSession.setPassword(App.settings.getSshPassword());
            sshSession.setConfig("StrictHostKeyChecking", "no"); // ask | yes | no
            sshSession.setServerAliveCountMax(App.settings.getSshAliveMaxCount());
            sshSession.setServerAliveInterval(App.settings.getSshAliveIntervalMs());
            sshSession.setDaemonThread(true);
            sshSession.connect();

            App.log.info("==== jPass tunnel connected.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Channel getStreamForwarder(String targetHost, int targetPort, boolean retrying)
            throws JSchException, IOException {
            try {
                Channel channel = sshSession.getStreamForwarder(targetHost, targetPort);
                channel.connect(App.settings.getSshChannelOpenTimeoutMs());
                return channel;
            } catch (JSchException ex) {
                if (!retrying && "session is down".equals(ex.getMessage())) {
                    App.log.info("Reconnecting SSH tunnel");
                    connect();
                    return getStreamForwarder(targetHost, targetPort, true);
                } else {
                    throw ex;
                }
            }
    }

    public static void disconnect() {
        if (sshSession != null) {
            try {
                sshSession.disconnect();
                App.log.info("==== SSH tunnel disconnected.");
            } catch (Exception ex) {
                App.log.info("exception: " + ex.getMessage());
                ex.printStackTrace();
            }
            sshSession = null;
        }
    }
}
