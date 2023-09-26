package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Assignee;
import si.matjazcerkvenik.pjm.model.Member;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.model.Task;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Formatter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
@SuppressWarnings("unused")
public class UiTaskBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 131146824402L;

    private static final Logger logger = LoggerFactory.getLogger(UiTaskBean.class);

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
        task.setLastModified(Formatter.getXmlGregorianCalendarNow());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("New status: " + selectedTaskStatus);
    }


    /* Assign to member */

    private String selectedAssignee;

    public String getSelectedAssignee() {
        return selectedAssignee;
    }

    public void setSelectedAssignee(String selectedAssignee) {
        this.selectedAssignee = selectedAssignee;
        Member member = project.getMembers().findMember(selectedAssignee);
        if (member == null) return;
        Assignee assignee = null;
        if (task.getAssignee().getMemberRefId() == null) { // FIXME tole neki ne dela
            assignee = new Assignee();
            assignee.setMemberRefId(member.getId());
        } else {
            task.getAssignee().setMemberRefId(member.getId());
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Assigned to " + selectedAssignee);
    }

    public List<String> getMembersList() {
        List<String> list = new ArrayList<>();
        for (Member m : project.getMembers().getList()) {
            list.add(m.getName() + " " + m.getLastName());
        }
        return list;
    }

    /*  Edit task  */

    public void saveDescription() {
        task.setLastModified(Formatter.getXmlGregorianCalendarNow());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Saved");
    }

    public String getRequirementTitle() {
        return task.getTitle();
    }

    public void setRequirementTitle(String title) {
        // TODO is this used anywhere or it should be called setTaskTitle???
        task.setTitle(title);
        DAO.getInstance().saveProject(project);
    }


}
