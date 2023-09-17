package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.*;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Formatter;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
@SuppressWarnings("unused")
public class UiRequirementBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 27734903592L;

    private static final Logger logger = LoggerFactory.getLogger(UiRequirementBean.class);
    private Requirement requirement;
    private boolean editModeOn = false;

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

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public boolean isEditModeOn() {
        return editModeOn;
    }

    public void setEditModeOn(boolean editModeOn) {
        this.editModeOn = editModeOn;
    }

    /*  Edit requirement  */


//    public String getRequirementId() {
//        return requirement.getId();
//    }
//
//    public void setRequirementId(String id) {
//        requirement.setId(id);
//        DAO.getInstance().saveProject(project);
//    }

    public String getRequirementTitle() {
        return requirement.getTitle();
    }

    public void setRequirementTitle(String title) {
        requirement.setTitle(title);
        saveProjectModifications("Saved");
    }



    public String deleteRequirement() {
        // FIXME tole še ne dela
        System.out.println("DELETE " + requirement.getTitle());
        return "project.xhtml?projectId=" + project.getId();
    }




    /*  Create new task  */

    private String newTskTitle;

    public String getNewTskTitle() {
        return newTskTitle;
    }

    public void setNewTskTitle(String newTskTitle) {
        this.newTskTitle = newTskTitle;
    }

    public void addNewTaskAction() {
        if (Formatter.isNullOrEmpty(newTskTitle)) return;
        Task t = new Task();
        t.setId(Formatter.getMd5ChecksumShortSalted(newTskTitle));
        t.setTitle(newTskTitle);
        t.setStatus(TaskStatus.DRAFT.label);
        t.setCreated(Formatter.getXmlGregorianCalendarNow());
        requirement.addNewTask(t);
        logger.info("addNewTaskAction: id: " + t.getId() + ", title: " + newTskTitle);
        newTskTitle = null;
        saveProjectModifications("New task created");
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
        if (Formatter.isNullOrEmpty(newCommentTitle)) return;
        Comment c = new Comment();
        c.setId(Formatter.getMd5ChecksumShortSalted(newCommentTitle));
        c.setDescription(newCommentTitle);
        c.setCreated(Formatter.getXmlGregorianCalendarNow());
        c.setLastModified(Formatter.getXmlGregorianCalendarNow());
        requirement.addNewComment(c);
        logger.info("addNewCommentAction: id: " + c.getId() + ", title: " + newCommentTitle);
        DAO.getInstance().saveProject(project);
        newCommentTitle = null;
        growlInfoMessage("Comment added");
    }

    public void editCommentAction(Comment comment) {
        comment.setLastModified(Formatter.getXmlGregorianCalendarNow());
        saveProjectModifications("Comment updated");
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

    // TODO modify comment and update the lastModified timestamp






    /* For tag selector menu */


    private List<String> selectedTagsList;

    public List<String> getSelectedTagsList() {
        if (selectedTagsList == null) selectedTagsList = new ArrayList<>();
        selectedTagsList.clear();
        if (requirement.getTags() == null)
            return new ArrayList<>();
        for (Tag tag : requirement.getTags().getList()) {
            Tag tagDef = tag2TagDef(tag);
            selectedTagsList.add(tagDef.getName());
        }
        return selectedTagsList;
    }

    public void setSelectedTagsList(List<String> selectedTagsList) {
        this.selectedTagsList = selectedTagsList;
//        if (requirement.getTags() == null) {
//            Tags tags = new Tags();
//            tags.setList(new ArrayList<>());
//            requirement.setTags(tags);
//        }
//        requirement.getTags().getList().clear();
//        for (String tagDef : selectedTagsList) {
//            Tag tag = tagDef2Tag(tagDef);
//            requirement.getTags().getList().add(tag);
//            System.out.println("setSelectedTagsList: " + tag.getRefId());
//        }
    }


    private Tag tagDef2Tag(String name) {
        Tag tagDef = null;
        for (Tag td : project.getTagDefinitions().getList()) {
            if (td.getName().equalsIgnoreCase(name)) {
                tagDef = td;
            }
        }
        Tag tag = new Tag();
        tag.setRefId(tagDef.getId());
        tag.setId(Formatter.getMd5ChecksumShortSalted(tagDef.getName() + tagDef.getColor()));
        return tag;
    }

    public void selectedTagChangeEvent(ValueChangeEvent event) {
        List<String> tagList = (List<String>) event.getNewValue();
        requirement.getTags().getList().clear();
        for (String tagDef : tagList) {
            Tag tag = tagDef2Tag(tagDef);
            requirement.getTags().getList().add(tag);
        }
        logger.info("tagList:" + tagList);
        DAO.getInstance().saveProject(project);
    }








    private String newIssueTitle;

    public String getNewIssueTitle() {
        return newIssueTitle;
    }

    public void setNewIssueTitle(String newIssueTitle) {
        this.newIssueTitle = newIssueTitle;
    }

    public void addNewIssueAction() {
        if (Formatter.isNullOrEmpty(newIssueTitle)) return;
        Issue issue = new Issue();
        issue.setId(Formatter.getMd5ChecksumShortSalted(newIssueTitle));
        issue.setProblem(newIssueTitle);
        issue.setCreated(Formatter.getXmlGregorianCalendarNow());
        requirement.addNewIssue(issue);
        logger.info("addNewIssueAction: id=" + issue.getId());
        DAO.getInstance().saveProject(project);
        newIssueTitle = null;
        growlInfoMessage("Issue added");
    }

    public void deleteIssueAction(String id) {
        for (Iterator<Issue> it = requirement.getIssues().getList().iterator(); it.hasNext();) {
            Issue issue = it.next();
            if (issue.getId().equals(id)) {
                it.remove();
                logger.info("deleteIssueAction: id=" + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Issue deleted");
    }


}
