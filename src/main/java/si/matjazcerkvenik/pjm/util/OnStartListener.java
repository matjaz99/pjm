package si.matjazcerkvenik.pjm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.DAO;
import si.matjazcerkvenik.pjm.Props;
import si.matjazcerkvenik.pjm.model.Project;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class OnStartListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(OnStartListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        Props.START_UP_TIME = System.currentTimeMillis();
        Props.RUNTIME_ID = UUID.randomUUID().toString();

        logger.info("");
        logger.info("************************************************");
        logger.info("*                                              *");
        logger.info("*                PJM started                   *");
        logger.info("*                                              *");
        logger.info("************************************************");
        logger.info("");

        // read version file
        InputStream inputStream = servletContextEvent.getServletContext().getResourceAsStream("/WEB-INF/version.txt");
        try {
            DataInputStream dis = new DataInputStream(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            Props.VERSION = br.readLine().trim();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Props.LOCAL_IP_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            Props.LOCAL_IP_ADDRESS = "unknown";
        }

        // is running inside docker
        try (Stream< String > stream =
                     Files.lines(Paths.get("/proc/1/cgroup"))) {
//            LogFactory.getLogger().info("Running in container: " + stream.anyMatch(line -> line.contains("/docker")));
            Props.IS_CONTAINERIZED = true;
        } catch (IOException e) {
//            LogFactory.getLogger().info("Running in container: false");
            Props.IS_CONTAINERIZED = false;
        }

        // print configuration
//        LogFactory.getLogger().info("VERSION=" + Props.VERSION);
//        LogFactory.getLogger().info("IPADDR=" + Props.LOCAL_IP_ADDRESS);
//        LogFactory.getLogger().info("RUNTIME_ID=" + Props.RUNTIME_ID);

        // load configuration from env vars
        Props.loadProps();

        // read and print all environment variables
//        LogFactory.getLogger().info("***** Environment variables *****");
        Map<String, String> map = System.getenv();
        for (Map.Entry <String, String> entry: map.entrySet()) {
//            LogFactory.getLogger().info(entry.getKey() + "=" + entry.getValue());
        }

        // initialize DAO
        DAO.getInstance();

        PjmMetrics.pjm_build_info.labels("PJM", Props.RUNTIME_ID, Props.VERSION, System.getProperty("os.name")).set(Props.START_UP_TIME);

//        File f = new File("./project_test.xml");
//        if (!f.exists()) logger.warn("NO FILE");
//        Project p = DAO.getInstance().loadProject("./project_test.xml");
//        logger.info("Project: " + p.getProjectName());

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
