package si.matjazcerkvenik.pjm.timertasks;

import si.matjazcerkvenik.pjm.model.*;
import si.matjazcerkvenik.pjm.util.Utils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class MonitoringTask extends Thread {

    private List<Project> projects;

    public MonitoringTask(List<Project> projects) {
        this.projects = projects;
        super.setName("MonitoringTask");
    }

    @Override
    public void run() {

        while (true) {

            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
            }

            for (Project p : projects) {
                for (Testbed t : p.getTestbeds().getList()) {
                    for (Service s : t.getServices()) {

                        if (Utils.isNullOrEmpty(s.getIpAddress())) s.setIpAddress(resolveIpAddress(s.getHostname()));
                        if (!s.isMonitoringActive()) continue;
                        s.setIcmpStatus(icmpPing(s.getHostname()));
                        s.setPortStatus(checkPort(s.getHostname(), s.getPort()));

                    }
                }
            }

        }

    }

    private String resolveIpAddress(String hostname) {
        String ip = "n/a";
        try {
            InetAddress ia = InetAddress.getByName(hostname);
            ip = ia.getHostAddress();
        } catch (UnknownHostException e) {
            ip = "UnknownHostException";
        } catch (Exception e) {
        }
        return ip;
    }

    private boolean icmpPing(String hostname) {
        boolean result = false;
        try {
            InetAddress ia = InetAddress.getByName(hostname);
            if (ia.isReachable(5000)) result = true;
        } catch (Exception e) {
        }
        return result;
    }

    private boolean checkPort(String hostname, String port) {
        boolean result = false;
        if (Utils.isNullOrEmpty(port)) return result;
        try (Socket sock = new Socket(hostname, Integer.parseInt(port))) {
            sock.close();
            result = true;
        } catch (ConnectException e) {
        } catch (IOException e) {
        }
        return result;
    }
}
