package si.matjazcerkvenik.pjm.web;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.ChecklistItem;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.timertasks.AlarmsTask;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Utils;
import si.matjazcerkvenik.pjm.util.Props;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

@ManagedBean(eager=true)
@ApplicationScoped
public class UiAppBean implements Serializable {

    private static final long serialVersionUID = 3794511649881L;

    private static final Logger logger = LoggerFactory.getLogger(UiAppBean.class);

    private List<Project> projects;

    protected Timer periodicTimer;

    public AlarmsTask alarmsTask;

    @PostConstruct
    public void init() {
        if (projects == null) {
            projects = DAO.getInstance().loadAllProjects();
            logger.info("init");
        }

        periodicTimer = new Timer("PeriodicTimer");
        alarmsTask = new AlarmsTask(projects);
        periodicTimer.schedule(alarmsTask, 30 * 1000, 5 * 60 * 1000);
        logger.info("periodic timer started");
    }

    public List<Project> getProjects() {
        return projects.stream()
                .filter(project -> checkProject(project))
                .collect(Collectors.toList());
    }

    private boolean checkProject(Project project) {
        if (radioButtonSelectedProjects.equals(Project.PROJECT_STATE_HIDDEN)) {
            // show only if hidden is selected
            if (project.getState().equals(Project.PROJECT_STATE_HIDDEN)) return true;
        } else {
            if (project.getState().equals(Project.PROJECT_STATE_HIDDEN)) return false;
        }

        if (radioButtonSelectedProjects.equals("All")) {
            return true;
        } else if (radioButtonSelectedProjects.equals(Project.PROJECT_STATE_PLANNING)) {
            if (project.getState().equals(Project.PROJECT_STATE_PLANNING)) return true;
        } else if (radioButtonSelectedProjects.equals(Project.PROJECT_STATE_ONHOLD)) {
            if (project.getState().equals(Project.PROJECT_STATE_ONHOLD)) return true;
        } else if (radioButtonSelectedProjects.equals(Project.PROJECT_STATE_ACTIVE)) {
            if (project.getState().equals(Project.PROJECT_STATE_ACTIVE)) return true;
        } else if (radioButtonSelectedProjects.equals(Project.PROJECT_STATE_COMPLETED)) {
            if (project.getState().equals(Project.PROJECT_STATE_COMPLETED)) return true;
        }
        return false;
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
        Project p = getProject(projectId);
        if (p != null && p.getId().equals(projectId)) {
            return p.calculateProgressOnProject();
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
        p.setState(Project.PROJECT_STATE_PLANNING);
        p.setId(Utils.getMd5ChecksumShortSalted(newProjectName));
        p.setProjectPath(Props.PJM_PROJECTS_DIRECTORY + "/" + newProjectName + ".xml");
        p.setCreated(Utils.getXmlGregorianCalendarNow());
        projects.add(p);
        DAO.getInstance().saveProject(p);
        newProjectName = null;
    }

    public void deleteProjectAction(Project p) {
        projects.remove(p);
        DAO.getInstance().deleteProject(p);
    }




    /* RADIO SELECTOR for projects */

    private String radioButtonSelectedProjects = "All";

    public String getRadioButtonSelectedProjects() {
        return radioButtonSelectedProjects;
    }

    public void setRadioButtonSelectedProjects(String radioButtonSelectedProjects) {
        this.radioButtonSelectedProjects = radioButtonSelectedProjects;
    }






    /* Quick links in header */

    public List<Project> getFavoriteProjects() {
        return projects.stream().filter(project -> project.isFavorite()).collect(Collectors.toList());
    }



    public String getHelp() {

        String filePath = "HELP.md";

        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            Parser parser = Parser.builder().build();
            // Node document = parser.parseReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            Node document = parser.parseReader(new InputStreamReader(input));
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            return renderer.render(document);
        } catch (IOException e) {
            logger.error("IOException: ", e);
        }
        return null;
    }
}
