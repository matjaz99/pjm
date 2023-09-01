package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.model.Task;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.MD5Checksum;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

@ManagedBean
@ViewScoped
@SuppressWarnings("unused")
public class UiTaskBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 131146824402L;

    private static final Logger logger = LoggerFactory.getLogger(UiTaskBean.class);

    @ManagedProperty(value="#{uiAppBean}")
    private UiAppBean uiAppBean;

    private Project project;
    private Requirement requirement;
    private Task task;

    private String selectedTaskStatus = "Draft";

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String projId = requestParameterMap.getOrDefault("projectId", null);
        String reqId = requestParameterMap.getOrDefault("reqId", null);
        String tskId = requestParameterMap.getOrDefault("tskId", null);
        project = uiAppBean.getProject(projId);
        for (Requirement r : project.getRequirements().getList()) {
            if (r.getId().equals(reqId)) {
                requirement = r;
                for (Task t : r.getTasks().getList()) {
                    if (t.getId().equals(tskId)) {
                        task = t;
                        break;
                    }
                }
                break;
            }
        }
        if (task.getStatus() != null) selectedTaskStatus = task.getStatus();
    }

    public UiAppBean getUiAppBean() {
        return uiAppBean;
    }

    public void setUiAppBean(UiAppBean uiAppBean) {
        this.uiAppBean = uiAppBean;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getSelectedTaskStatus() {
        return selectedTaskStatus;
    }

    public void setSelectedTaskStatus(String selectedTaskStatus) {
        this.selectedTaskStatus = selectedTaskStatus;
        task.setStatus(selectedTaskStatus);
        DAO.getInstance().saveProject(project);
        growlInfoMessage("New status: " + selectedTaskStatus);
    }

    /*  Edit task  */

    public void saveDescription() {
        logger.info("saveDescription: " + task.getDescription());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Saved");
    }

    public String getRequirementTitle() {
        return task.getTitle();
    }

    public void setRequirementTitle(String title) {
        task.setTitle(title);
        DAO.getInstance().saveProject(project);
    }


}
