package si.matjazcerkvenik.pjm.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Alarm;
import si.matjazcerkvenik.pjm.model.Project;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO {

    private static final Logger logger = LoggerFactory.getLogger(DAO.class);
    private static DAO instance;

    /** A map of projects (IDs) and their alarm maps (alarm ID / alarm) */
    private Map<String, Map<String, Alarm>> alarmMap = new HashMap<>();

    private DAO() {
    }

    public static DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    public List<Project> loadAllProjects() {

        List<Project> list = new ArrayList<>();

        File dir = new File(Props.PJM_PROJECTS_DIRECTORY);
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getAbsolutePath().endsWith(".xml");
            }
        });

        logger.info("DAO:loadAllProjects: " + files.length + " projects found");

        for (int i = 0; i < files.length; i++) {
            Project p = loadProject(files[i].getAbsolutePath());
            list.add(p);
        }
        return list;
    }

    private Project loadProject(String path) {
        
        Project project = null;

        try {

            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            project = (Project) jaxbUnmarshaller.unmarshal(file);

            project.setProjectPath(file.getAbsolutePath());

            logger.info("DAO:loadProject: " + file.getAbsolutePath());

        } catch (JAXBException e) {
            logger.error("DAO:loadProject: JAXBException: ", e);
        }

        return project;

    }

    public void saveProject(Project project) {

        try {

            File file = new File(project.getProjectPath());
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(project, file);

            logger.info("DAO:saveProject: " + project.getName() + " on path: " + file.getAbsolutePath());

        } catch (JAXBException e) {
            logger.error("DAO:saveProject: JAXBException: ", e);
        }

    }

    public void deleteProject(Project project) {
        File file = new File(project.getProjectPath());
        file.delete();
        logger.info("DAO:deleteProject: " + project.getName() + " on path: " + file.getAbsolutePath());
    }

    public void raiseAlarm(String projectId, Alarm a) {
        if (!alarmMap.containsKey(projectId)) alarmMap.put(projectId, new HashMap<>());
        if (!alarmMap.get(projectId).containsKey(a.getId())) alarmMap.get(projectId).put(a.getId(), a);
    }

    public void clearAlarm(String projectId, Alarm a) {
        if (!alarmMap.containsKey(projectId)) return;
        if (alarmMap.get(projectId).containsKey(a.getId())) alarmMap.get(projectId).remove(a.getId());
    }

    public List<Alarm> getAlarmList(String projectId) {
        if (!alarmMap.containsKey(projectId)) return new ArrayList<>();
        return new ArrayList<>(alarmMap.get(projectId).values());
    }

}
