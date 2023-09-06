package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.PeriodicTask;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Props;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.util.MD5Checksum;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import java.util.Timer;

@ManagedBean(eager=true)
@ApplicationScoped
public class UiAppBean implements Serializable {

    private static final long serialVersionUID = 3794511649881L;

    private static final Logger logger = LoggerFactory.getLogger(UiAppBean.class);

    private List<Project> projects;

    protected Timer periodicTimer = null;
    private PeriodicTask periodicTask = null;

    @PostConstruct
    public void init() {
        if (projects == null) {
            projects = DAO.getInstance().loadAllProjects();
            logger.info("UiAppBean:init");
        }

        periodicTimer = new Timer("PeriodicTimer");
        periodicTask = new PeriodicTask(projects);
        periodicTimer.schedule(periodicTask, 30 * 1000, 5 * 60 * 1000);
        logger.info("periodic timer started");
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Project getProject(String id) {
        for (Project p : projects) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    private String newProjectName;

    public String getNewProjectName() {
        return newProjectName;
    }

    public void setNewProjectName(String newProjectName) {
        this.newProjectName = newProjectName;
    }

    public void addNewProjectAction() {
        if (newProjectName == null) {
            logger.warn("addNewProjectAction: name is null");
            return;
        }
        Project p = new Project();
        p.setName(newProjectName);
        p.setId(MD5Checksum.getMd5ChecksumShortSalted(newProjectName));
        p.setProjectPath(Props.PJM_PROJECTS_DIRECTORY + "/" + newProjectName + ".xml");
        projects.add(p);
        DAO.getInstance().saveProject(p);
        newProjectName = null;
    }

    public void deleteProjectAction(Project p) {
        projects.remove(p);
        DAO.getInstance().deleteProject(p);
    }

}
