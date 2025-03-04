package si.matjazcerkvenik.pjm.timertasks;

import si.matjazcerkvenik.pjm.model.*;
import si.matjazcerkvenik.pjm.util.Utils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.TimerTask;

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

                        s.setIcmpStatus(icmp(s.getHostname()));
                        s.setPortStatus(checkPort(s.getHostname(), s.getPort()));

                    }
                }
            }


        }


    }

    private boolean icmp(String hostname) {
        try {
            InetAddress ia = InetAddress.getByName(hostname);
            if (ia.isReachable(5000)) return true;
        } catch (UnknownHostException e) {
//            System.out.println("UnknownHostException: " + hostname + "; " + e.getMessage());
        } catch (IOException e) {
//            System.out.println("IOException: " + e.getMessage());
        }
        return false;
    }

    private boolean checkPort(String hostname, String port) {
        if (Utils.isNullOrEmpty(port)) return false;
        try (Socket sock = new Socket(hostname, Integer.parseInt(port))) {
            sock.close();
            return true;
        } catch (ConnectException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
