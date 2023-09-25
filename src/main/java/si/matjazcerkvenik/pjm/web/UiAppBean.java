package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.model.Task;
import si.matjazcerkvenik.pjm.model.TaskStatus;
import si.matjazcerkvenik.pjm.timertasks.PeriodicTask;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Formatter;
import si.matjazcerkvenik.pjm.util.Props;

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

    protected Timer periodicTimer;

    public PeriodicTask periodicTask;

    @PostConstruct
    public void init() {
        if (projects == null) {
            projects = DAO.getInstance().loadAllProjects();
            logger.info("init");
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

    /**
     * This method works slightly different that <code>Ui.ProjectBean.calculateProgressOnRequirement</code>
     * because it takes into account also requirements without tasks. In this case it is assumed that
     * requirement is not fulfilled and counts as 1 incomplete task.
     * @param projectId
     * @return progress in percentage
     */
    public int calculateProgressOnProject(String projectId) {
        for (Project p : projects) {
            if (p.getId().equals(projectId)) {
                return p.calculateProgressOnProject();
            }
        }
        return 0;
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
        p.setId(Formatter.getMd5ChecksumShortSalted(newProjectName));
        p.setProjectPath(Props.PJM_PROJECTS_DIRECTORY + "/" + newProjectName + ".xml");
        p.setCreated(Formatter.getXmlGregorianCalendarNow());
        projects.add(p);
        DAO.getInstance().saveProject(p);
        newProjectName = null;
    }

    public void deleteProjectAction(Project p) {
        projects.remove(p);
        DAO.getInstance().deleteProject(p);
    }

}
