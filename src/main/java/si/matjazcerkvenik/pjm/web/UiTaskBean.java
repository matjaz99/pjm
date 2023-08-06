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
public class UiTaskBean implements Serializable {

    private static final long serialVersionUID = 131646875422L;

    private static final Logger logger = LoggerFactory.getLogger(UiTaskBean.class);

    @ManagedProperty(value="#{uiAppBean}")
    private UiAppBean uiAppBean;

    private Project project;
    private Requirement requirement;
    private Task task;

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

    }

    public UiAppBean getUiAppBean() {
        return uiAppBean;
    }

    public void setUiAppBean(UiAppBean uiAppBean) {
        this.uiAppBean = uiAppBean;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }


    /*  Edit requirement  */

    public void saveDescription() {
        logger.info("saveDescription: " + requirement.getDescription());
        DAO.getInstance().saveProject(project);
    }

    public String getRequirementId() {
        return requirement.getId();
    }

    public void setRequirementId(String id) {
        requirement.setId(id);
        DAO.getInstance().saveProject(project);
    }

    public String getRequirementTitle() {
        return requirement.getTitle();
    }

    public void setRequirementTitle(String title) {
        requirement.setTitle(title);
        DAO.getInstance().saveProject(project);
    }



    /*  Create new task  */

    private String newTskId;
    private String newTskTitle;

    public String getNewTskId() {
        return newTskId;
    }

    public void setNewTskId(String newTskId) {
        this.newTskId = newTskId;
    }

    public String getNewTskTitle() {
        return newTskTitle;
    }

    public void setNewTskTitle(String newTskTitle) {
        this.newTskTitle = newTskTitle;
    }

    public void addNewTaskAction() {
        logger.info("addNewReqAction: new tsk id: " + newTskId + ", title: " + newTskTitle);
        if (Utils.isNullOrEmpty(newTskTitle)) return;
        if (Utils.isNullOrEmpty(newTskId)) newTskId = MD5Checksum.getMd5ChecksumShort(newTskTitle);
        Task t = new Task();
        t.setId(newTskId);
        t.setTitle(newTskTitle);
        requirement.addNewTask(t);
        logger.info("addNewReqAction: new tsk: " + newTskId);
        DAO.getInstance().saveProject(project);
        newTskId = null;
        newTskTitle = null;
    }

    public void deleteTaskAction(String id) {
        for (Iterator<Task> it = requirement.getTasks().getList().iterator(); it.hasNext();) {
            Task t = it.next();
            if (t.getId().equals(id)) {
                it.remove();
                logger.info("deleteTaskAction: tsk: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
    }

}
