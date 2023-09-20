package si.matjazcerkvenik.pjm.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.*;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class DAO {

    private static final Logger logger = LoggerFactory.getLogger(DAO.class);
    private static DAO instance;

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

        logger.info(files.length + " projects found");

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

            logger.info(file.getAbsolutePath());


            for (Requirement req : project.getRequirements().getList()) {

                // make sure every requirement has created timestamp
                if (req.getCreated() == null) req.setCreated(Formatter.getXmlGregorianCalendarNow());


                for (Task task : req.getTasks().getList()) {

                    // every task must have created timestamp; if not use req created time
                    if (task.getCreated() == null) task.setCreated(req.getCreated());

                    // every task must have lastModified; if not use created time
                    if (task.getLastModified() == null) task.setLastModified(task.getCreated());

                    // every task must have status; if not use draft as default
                    if (task.getStatus() == null) task.setStatus(TaskStatus.DRAFT.label);

                }
            }

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

            logger.info(project.getName() + " on path: " + file.getAbsolutePath());

        } catch (JAXBException e) {
            logger.error("DAO:saveProject: JAXBException: ", e);
        }

    }

    public void deleteProject(Project project) {
        File file = new File(project.getProjectPath());
        file.delete();
        logger.info(project.getName() + " on path: " + file.getAbsolutePath());
    }



//    public List<Alarm> getAlarmList(String projectId) {
//        if (!alarmMap.containsKey(projectId)) return new ArrayList<>();
//        return new ArrayList<>(alarmMap.get(projectId).values());
//    }

}
