package si.matjazcerkvenik.pjm;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Project;

//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class DAO {

    private static DAO instance;
    private static final Logger logger = LoggerFactory.getLogger(DAO.class);

    private DAO() {
    }

    public static DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    public List<String> loadAllProjects() {
        // TODO load all projects
        return null;
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
            project.setProjectId(project.hashCode() + "");

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
