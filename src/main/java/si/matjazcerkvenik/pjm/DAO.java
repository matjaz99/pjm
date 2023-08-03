package si.matjazcerkvenik.pjm;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.util.MD5Checksum;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO {

    private static DAO instance;
    private static final Logger logger = LoggerFactory.getLogger(DAO.class);

    private Map<String, Project> projects = new HashMap<>();

    private DAO() {
    }

    public static DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    public void loadAllProjects() {
        File dir = new File("projects");
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getAbsolutePath().endsWith(".xml");
            }
        });

        for (int i = 0; i < files.length; i++) {
            Project p = loadProject(files[i].getAbsolutePath());
            projects.put(p.getProjectId(), p);
            logger.info("DAO:loadAllProjects: adding " + files[i].getAbsolutePath());
        }
    }

    public Project loadProject(String path) {
        
        Project project = null;

        try {

            File file = new File(path);
//            if (!file.exists()) {
//                sshClients = new SshClients();
//                JAXBContext jaxbContext = JAXBContext
//                        .newInstance(SshClients.class);
//                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
//                        true);
//                jaxbMarshaller.marshal(sshClients, file);
//            }
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            project = (Project) jaxbUnmarshaller.unmarshal(file);

            project.setProjectPath(file.getAbsolutePath());
            project.setProjectId(MD5Checksum.getMd5Checksum(project.getProjectName()));

            logger.info("DAO:loadProject: " + file.getAbsolutePath());

        } catch (JAXBException e) {
            logger.error("DAO:loadProject: JAXBException: ", e);
        }

        return project;

    }

    public void saveProject(Project project) {

        try {

            logger.info("DAO:saveProject1: " + project.getProjectName());

            File file = new File(project.getProjectPath());
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(project, file);

            logger.info("DAO:saveProject: " + file.getAbsolutePath());

        } catch (JAXBException e) {
            logger.error("DAO:saveProject: JAXBException: ", e);
        }

    }

}
