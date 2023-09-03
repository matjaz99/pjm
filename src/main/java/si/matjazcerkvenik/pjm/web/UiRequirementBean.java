package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.*;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Formatter;
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
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
@SuppressWarnings("unused")
public class UiRequirementBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 27734903592L;

    private static final Logger logger = LoggerFactory.getLogger(UiRequirementBean.class);

    @ManagedProperty(value="#{uiAppBean}")
    private UiAppBean uiAppBean;
    
    private Requirement requirement;
    private Project project;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String projId = requestParameterMap.getOrDefault("projectId", null);
        String reqId = requestParameterMap.getOrDefault("reqId", null);
        project = uiAppBean.getProject(projId);

        if (project.getRequirements() == null) return;
        for (Requirement r : project.getRequirements().getList()) {
            if (r.getId().equals(reqId)) {
                requirement = r;
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


    /*  Edit requirement  */

    public void saveDescription() {
        logger.info("saveDescription: " + requirement.getDescription());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Saved");
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

//    public String convertStatusToSeverity(String status) {
//        if (status.equalsIgnoreCase("Draft")) {
//            return "primary";
//        } else if (status.equalsIgnoreCase("In progress")) {
//            return "info";
//        } else if (status.equalsIgnoreCase("Clarify")) {
//            return "warning";
//        } else if (status.equalsIgnoreCase("Waiting")) {
//            return "danger";
//        } else if (status.equalsIgnoreCase("Complete")) {
//            return "success";
//        }
//        return "primary";
//    }



    /*  Create new task  */

    private String newTskTitle;

    public String getNewTskTitle() {
        return newTskTitle;
    }

    public void setNewTskTitle(String newTskTitle) {
        this.newTskTitle = newTskTitle;
    }

    public void addNewTaskAction() {
        if (Utils.isNullOrEmpty(newTskTitle)) return;
        Task t = new Task();
        t.setId(MD5Checksum.getMd5ChecksumShortSalted(newTskTitle));
        t.setTitle(newTskTitle);
        t.setStatus(TaskStatus.DRAFT.label);
        requirement.addNewTask(t);
        logger.info("addNewTaskAction: id: " + t.getId() + ", title: " + newTskTitle);
        DAO.getInstance().saveProject(project);
        newTskTitle = null;
        growlInfoMessage("New task created");
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
        growlInfoMessage("Task deleted");
    }





    /*  Create new remark  */

    private String newCommentTitle;

    public String getNewCommentTitle() {
        return newCommentTitle;
    }

    public void setNewCommentTitle(String newCommentTitle) {
        this.newCommentTitle = newCommentTitle;
    }

    public void addNewCommentAction() {
        if (Utils.isNullOrEmpty(newCommentTitle)) return;
        Comment c = new Comment();
        c.setId(MD5Checksum.getMd5ChecksumShortSalted(newCommentTitle));
        c.setDescription(newCommentTitle);
        c.setLastModified(Formatter.getXmlGregorianCalendarNow());
        requirement.addNewComment(c);
        logger.info("addNewCommentAction: id: " + c.getId() + ", title: " + newCommentTitle);
        DAO.getInstance().saveProject(project);
        newCommentTitle = null;
        growlInfoMessage("Comment added");
    }

    public void deleteCommentAction(String id) {
        for (Iterator<Comment> it = requirement.getComments().getList().iterator(); it.hasNext();) {
            Comment c = it.next();
            if (c.getId().equals(id)) {
                it.remove();
                logger.info("deleteCommentAction: tsk: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Comment deleted");
    }




    /* For tag selector menu */

    private List<Tag> tagsList;
    private List<Tag> selectedTagsList;

    public List<Tag> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<Tag> tagsList) {
        this.tagsList = tagsList;
    }

    public List<Tag> getSelectedTagsList() {
        return selectedTagsList;
    }

    public void setSelectedTagsList(List<Tag> selectedTagsList) {
        this.selectedTagsList = selectedTagsList;
    }


}
