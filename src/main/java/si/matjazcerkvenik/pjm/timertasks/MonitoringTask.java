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
                Thread.sleep(15 * 1000);
            } catch (InterruptedException e) {

            }

            System.out.println("Periodic monitoring started");

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
        boolean result = false;
        try {
            InetAddress ia = InetAddress.getByName(hostname);
            if (ia.isReachable(5000)) result = true;
        } catch (UnknownHostException e) {
//            System.out.println("UnknownHostException: " + hostname + "; " + e.getMessage());
        } catch (IOException e) {
//            System.out.println("IOException: " + e.getMessage());
        }
        System.out.println("Ping: " + hostname + "; " + result);
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
        System.out.println("CheckPort: " + hostname + ":" + port + "; " + result);
        return result;
    }
}
