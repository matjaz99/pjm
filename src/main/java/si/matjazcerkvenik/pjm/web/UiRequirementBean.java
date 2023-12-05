package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.*;
import si.matjazcerkvenik.pjm.util.DAO;
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
        logger.info("loaded: " + project.getName());

        if (project.getRequirements() == null) return;
        for (Requirement r : project.getRequirements().getList()) {
            if (r.getId().equals(reqId)) {
                requirement = r;
                break;
            }
        }

        editModeOn = false;

    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }



    /*  Edit requirement  */


    public boolean isEditModeOn() {
        return editModeOn;
    }

    public void setEditModeOn(boolean editModeOn) {
        this.editModeOn = editModeOn;
    }

    public void toggleEditMode() {
        editModeOn = !editModeOn;
    }


    public String getRequirementTitle() {
        return requirement.getTitle();
    }

    public void setRequirementTitle(String title) {
        requirement.setTitle(title);
        project.addHistoryItem(new HistoryItem(requirement.getTitle(), "/pjm/project/requirement.xhtml?projectId=" + project.getId() + "&reqId=" + requirement.getId(), "REQ", "Requirement title changed"));
        saveProjectModifications("Saved");
    }

    /**
     * Called from requirement.xhtml when description is changed and Save button is pressed.
     */
    public void saveRequirementChanges() {
        editModeOn = !editModeOn;
        requirement.setLastModified(Utils.getXmlGregorianCalendarNow());
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
        if (Utils.isNullOrEmpty(newTskTitle)) return;
        Task t = new Task();
        t.setId(Utils.getMd5ChecksumShortSalted(newTskTitle));
        t.setTitle(newTskTitle);
        t.setStatus(TaskStatus.DRAFT.label);
        t.setCreated(Utils.getXmlGregorianCalendarNow());
        requirement.addNewTask(t);
        requirement.setLastModified(Utils.getXmlGregorianCalendarNow());
        logger.info("addNewTaskAction: id: " + t.getId() + ", title: " + newTskTitle);
        newTskTitle = null;
        project.addHistoryItem(new HistoryItem(t.getTitle(), "/pjm/project/task.xhtml?projectId=" + project.getId() + "&reqId=" + requirement.getId() + "&tskId=" + t.getId(), "TASK", "New task created"));
        saveProjectModifications("New task created");
    }

    public void deleteTaskAction(String id) {
        for (Iterator<Task> it = requirement.getTasks().getList().iterator(); it.hasNext();) {
            Task t = it.next();
            if (t.getId().equals(id)) {
                it.remove();
                project.addHistoryItem(new HistoryItem(t.getTitle(), "/pjm/project/requirement.xhtml?projectId=" + project.getId() + "&reqId=" + requirement.getId(), "TASK", "Task deleted"));
                logger.info("deleteTaskAction: tsk: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Task deleted");
    }

    public String getAssigneeAvatar(String memberId) {
        Member member = findMember(memberId);
        return member.getName() + " " + member.getLastName();
    }

    public void addNewCustomTaskAction(String title) {
        if (Utils.isNullOrEmpty(title)) return;
        Task t = new Task();
        t.setId(Utils.getMd5ChecksumShortSalted(title));
        t.setTitle(title);
        t.setStatus(TaskStatus.DRAFT.label);
        t.setCreated(Utils.getXmlGregorianCalendarNow());
        requirement.addNewTask(t);
        requirement.setLastModified(Utils.getXmlGregorianCalendarNow());
        logger.info("addNewCustomTaskAction: id: " + t.getId() + ", title: " + title);
        project.addHistoryItem(new HistoryItem(t.getTitle(), "/pjm/project/task.xhtml?projectId=" + project.getId() + "&reqId=" + requirement.getId() + "&tskId=" + t.getId(), "TASK", "New task created"));
        saveProjectModifications("New task created");
    }








    /*  Create new comment  */

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
        c.setId(Utils.getMd5ChecksumShortSalted(newCommentTitle));
        c.setDescription(newCommentTitle);
        c.setCreated(Utils.getXmlGregorianCalendarNow());
        c.setLastModified(Utils.getXmlGregorianCalendarNow());
        requirement.addNewComment(c);
        requirement.setLastModified(Utils.getXmlGregorianCalendarNow());
        logger.info("addNewCommentAction: id: " + c.getId() + ", title: " + newCommentTitle);
        DAO.getInstance().saveProject(project);
        newCommentTitle = null;
        project.addHistoryItem(new HistoryItem("-", "/pjm/project/requirement.xhtml?projectId=" + project.getId() + "&reqId=" + requirement.getId(), "COMMENT", "Comment added"));
        growlInfoMessage("Comment added");
    }

    public void editCommentAction(Comment comment) {
        comment.setLastModified(Utils.getXmlGregorianCalendarNow());
        requirement.setLastModified(Utils.getXmlGregorianCalendarNow());
        project.addHistoryItem(new HistoryItem("-", "/pjm/project/requirement.xhtml?projectId=" + project.getId() + "&reqId=" + requirement.getId(), "COMMENT", "Comment modified"));
        saveProjectModifications("Comment updated");
    }

    public void deleteCommentAction(String id) {
        for (Iterator<Comment> it = requirement.getComments().getList().iterator(); it.hasNext();) {
            Comment c = it.next();
            if (c.getId().equals(id)) {
                it.remove();
                project.addHistoryItem(new HistoryItem("-", "/pjm/project/requirement.xhtml?projectId=" + project.getId() + "&reqId=" + requirement.getId(), "COMMENT", "Comment deleted"));
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
        tag.setId(Utils.getMd5ChecksumShortSalted(tagDef.getName() + tagDef.getColor()));
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
        if (Utils.isNullOrEmpty(newIssueTitle)) return;
        Issue issue = new Issue();
        issue.setId(Utils.getMd5ChecksumShortSalted(newIssueTitle));
        issue.setProblem(newIssueTitle);
        issue.setCreated(Utils.getXmlGregorianCalendarNow());
        requirement.addNewIssue(issue);
        requirement.setLastModified(Utils.getXmlGregorianCalendarNow());
        logger.info("addNewIssueAction: id=" + issue.getId());
        DAO.getInstance().saveProject(project);
        newIssueTitle = null;
        project.addHistoryItem(new HistoryItem("-", "/pjm/project/requirement.xhtml?projectId=" + project.getId() + "&reqId=" + requirement.getId(), "ISSUE", "New issue created"));
        growlInfoMessage("Issue added");
    }

    public void deleteIssueAction(String id) {
        for (Iterator<Issue> it = requirement.getIssues().getList().iterator(); it.hasNext();) {
            Issue issue = it.next();
            if (issue.getId().equals(id)) {
                it.remove();
                logger.info("deleteIssueAction: id=" + id);
                project.addHistoryItem(new HistoryItem("-", "/pjm/project/requirement.xhtml?projectId=" + project.getId() + "&reqId=" + requirement.getId(), "ISSUE", "Issue deleted"));
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Issue deleted");
    }


}
