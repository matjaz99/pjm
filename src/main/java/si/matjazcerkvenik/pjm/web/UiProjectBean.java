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
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class UiProjectBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 22483045884L;

    private static final Logger logger = LoggerFactory.getLogger(UiProjectBean.class);

    private String searchText;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
    }


    public String getProjectName() {
        return project.getName();
    }

    /**
     * Change the name of the project
     * @param s name
     */
    public void setProjectName(String s) {
        project.setName(s);
        DAO.getInstance().saveProject(project);
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * Apply searchText and return requirements. If searchText is null, return all.
     * @return list
     */
    public List<Requirement> getRequirements() {
        if (searchText == null || searchText.trim().length() == 0) return project.getRequirements().getList();
        // use map to avoid duplicates
        Map<String, Requirement> map = new HashMap<>();
        for (Requirement r : project.getRequirements().getList()) {

            // TODO support operators AND and OR

            if (searchText.startsWith("REQ:")) {

                // TODO search by request title only

            } else if (searchText.startsWith("TAG:")) {

                // TODO

            } else if (searchText.startsWith("ASSIGNEE:")) {

                // TODO

            } else {
                // search all fields
                if (r.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                    if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                }
                if (r.getDescription() != null && r.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    // TODO clean description of html code (from text formatting) or search will not work
                    if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                }

                // search tasks
                for (Task t : r.getTasks().getList()) {
                    if (t.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                    }
                    if (t.getDescription() != null && t.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                    }
                }
            }

        }
        return new ArrayList<>(map.values());
    }

    public int calculateProgressOnRequirement(String reqId) {
        for (Requirement r : project.getRequirements().getList()) {
            if (r.getId().equals(reqId)) {
                return r.calculateProgressOnRequirement();
            }
        }
        return 0;
    }

    public boolean hasRequirementOpenIssues(String reqId) {
        for (Requirement r : project.getRequirements().getList()) {
            if (r.getId().equals(reqId)) {
                if (r.getIssues().getList().size() > 0) return true;
            }
        }
        return false;
    }



    /* Handling of new requirement dialog */

    private String newReqTitle;

    public String getNewReqTitle() {
        return newReqTitle;
    }

    public void setNewReqTitle(String newReqTitle) {
        this.newReqTitle = newReqTitle;
    }

    public void addNewReqAction() {
        if (Utils.isNullOrEmpty(newReqTitle)) return;
        Requirement r = new Requirement();
        r.setId(Utils.getMd5ChecksumShortSalted(newReqTitle));
        r.setTitle(newReqTitle);
        r.setCreated(Utils.getXmlGregorianCalendarNow());
        project.addNewRequirement(r);
        logger.info("new req: " + r.getId() + ", title: " + newReqTitle);
        DAO.getInstance().saveProject(project);
        newReqTitle = null;
        growlInfoMessage("New requirement created");
    }

    public void deleteReqAction(String id) {
        for (Iterator<Requirement> it = project.getRequirements().getList().iterator(); it.hasNext();) {
            Requirement r = it.next();
            if (r.getId().equals(id)) {
                it.remove();
                logger.info("deleteReqAction: req: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Requirement deleted");
    }






    /* MEMBERS */

    private String newMemberName;

    private String newMemberLastName;

    public String getNewMemberName() {
        return newMemberName;
    }

    public void setNewMemberName(String newMemberName) {
        this.newMemberName = newMemberName;
    }

    public String getNewMemberLastName() {
        return newMemberLastName;
    }

    public void setNewMemberLastName(String newMemberLastName) {
        this.newMemberLastName = newMemberLastName;
    }

    public void addNewMemberAction() {
        if (Utils.isNullOrEmpty(newMemberName)) return;
        Member m = new Member();
        m.setId(Utils.getMd5ChecksumShortSalted(newMemberName));
        m.setName(newMemberName);
        m.setLastName(newMemberLastName);
        m.setRole("");
        project.addNewMember(m);
        logger.info("new member: " + m.getId() + ", name: " + newMemberName);
        DAO.getInstance().saveProject(project);
        newMemberName = null;
        newMemberLastName = null;
        growlInfoMessage("New member joined");
    }

    public void deleteMemberAction(Member member) {
        for (Iterator<Member> it = project.getMembers().getList().iterator(); it.hasNext();) {
            Member m = it.next();
            if (m.getId().equals(member.getId())) {
                it.remove();
                logger.info("member: " + member.getId());
                break;
            }
        }

        // also delete members (assignees) on tasks
        for (Requirement r : project.getRequirements().getList()) {
            for (Task t : r.getTasks().getList()) {
                if (t.getAssignee().getMemberRefId() != null
                        && t.getAssignee().getMemberRefId().equalsIgnoreCase(member.getId())) {
                    t.getAssignee().setMemberRefId(null);
                }
            }
        }

        DAO.getInstance().saveProject(project);
        growlInfoMessage("Member fired");
    }

    public void selectedMemberRoleChangeEvent(Member member) {
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Member promoted to " + member.getRole());
    }




    // HISTORY


    public List<Object> getHistoryItems() {
        return null;
    }


}
