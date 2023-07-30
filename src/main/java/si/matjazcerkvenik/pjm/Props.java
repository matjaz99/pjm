package si.matjazcerkvenik.pjm;

import java.io.File;

public class Props {

    public static String RUNTIME_ID = "0000-0000-0000-0000";
    public static long START_UP_TIME = 0;
    public static String VERSION = "n/a";
    public static boolean IS_CONTAINERIZED = false;
    public static String LOCAL_IP_ADDRESS;

    public static String PJM_PROJECTS_DIRECTORY;
    public static String DATE_FORMAT = "yyyy/MM/dd H:mm:ss";

    /**
     * Read configuration from environment variables
     */
    public static void loadProps() {

        PJM_PROJECTS_DIRECTORY = System.getenv().getOrDefault("PJM_PROJECTS_DIRECTORY", "/opt/pjm/projects").trim();

        if (new File("/Users/matjaz").exists()) {
            PJM_PROJECTS_DIRECTORY = "/Users/matjaz/Library/CloudStorage/Dropbox/monis/other_stacks/pjm/projects";
        }

    }

}
