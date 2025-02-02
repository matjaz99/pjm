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
        if (this.searchText.length() == 0) this.searchText = null;
    }

    /**
     * Apply searchText and return requirements. If searchText is null, return all.
     * @return list
     */
    public List<Requirement> getRequirements() {
        if (Utils.isNullOrEmpty(searchText)) return project.getRequirements().getList();
        // use map to avoid duplicates
        Map<String, Requirement> map = new HashMap<>();
        for (Requirement r : project.getRequirements().getList()) {

            // TODO support operators AND and OR

            if (searchText.startsWith("TAG:")) {

                String[] searchTextArray = searchText.split(":");
                if (searchTextArray.length < 2) return new ArrayList<>(map.values());
                String searchTag = searchTextArray[1].trim();
                // get tag id
                String tagId = null;
                for (Tag t : project.getTagDefinitions().getList()) {
                    if (t.getName().toLowerCase().startsWith(searchTag.toLowerCase())) {
                        tagId = t.getId();
                    }
                }
                // search requirements for tag with refId
                for (Tag t : r.getTags().getList()) {
                    if (tagId != null && tagId.equals(t.getRefId())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                    }
                }

            } else if (searchText.startsWith("ASSIGNEE:") || searchText.startsWith("ASS:")) {

                String[] searchTextArray = searchText.split(":");
                if (searchTextArray.length < 2) return new ArrayList<>(map.values());
                String searchAssignee = searchTextArray[1].trim();
                for (Member m : project.getMembers().getList()) {
                    // first search members to get member ID
                    if (m.getName().startsWith(searchAssignee) || m.getLastName().startsWith(searchAssignee)) {
                        for (Task t : r.getTasks().getList()) {
                            if (t.getAssignee() != null && t.getAssignee().getMemberRefId() != null && t.getAssignee().getMemberRefId().equalsIgnoreCase(m.getId())) {
                                if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                            }
                        }
                    }
                }


            } else if (searchText.startsWith("GROUP:")) {

                String[] searchTextArray = searchText.split(":");
                if (searchTextArray.length < 2) return new ArrayList<>(map.values());
                String searchGroup = searchTextArray[1].trim();
                if (r.getGroup().toLowerCase().startsWith(searchGroup)) {
                    if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                }

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

                // search issues
                for (Issue i : r.getIssues().getList()) {
                    if (!Utils.isNullOrEmpty(i.getProblem()) && i.getProblem().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                    }
                    if (!Utils.isNullOrEmpty(i.getSolution()) && i.getSolution().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                    }
                }

                // search comments
                for (Comment c : r.getComments().getList()) {
                    if (!Utils.isNullOrEmpty(c.getDescription()) && c.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r);
                    }
                }
            }

        }
        return new ArrayList<>(map.values());
    }

    public List<AbstractItem> getAbstractItems() {
//        if (Utils.isNullOrEmpty(searchText)) return project.getRequirements().getList();
        // use map to avoid duplicates
        Map<String, AbstractItem> map = new HashMap<>();
        for (Requirement r : project.getRequirements().getList()) {

            // TODO support operators AND and OR

            if (searchText.startsWith("TAG:")) {

                String[] searchTextArray = searchText.split(":");
                if (searchTextArray.length < 2) return new ArrayList<>(map.values());
                String searchTag = searchTextArray[1].trim();
                // get tag id
                String tagId = null;
                for (Tag t : project.getTagDefinitions().getList()) {
                    if (t.getName().toLowerCase().startsWith(searchTag.toLowerCase())) {
                        tagId = t.getId();
                    }
                }
                // search requirements for tag with refId
                for (Tag t : r.getTags().getList()) {
                    if (tagId != null && tagId.equals(t.getRefId())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r.toAbstractItem());
                    }
                }

            } else if (searchText.startsWith("ASSIGNEE:") || searchText.startsWith("ASS:")) {

                String[] searchTextArray = searchText.split(":");
                if (searchTextArray.length < 2) return new ArrayList<>(map.values());
                String searchAssignee = searchTextArray[1].trim();
                for (Member m : project.getMembers().getList()) {
                    // first search members to get member ID
                    if (m.getName().startsWith(searchAssignee) || m.getLastName().startsWith(searchAssignee)) {
                        for (Task t : r.getTasks().getList()) {
                            if (t.getAssignee() != null && t.getAssignee().getMemberRefId() != null && t.getAssignee().getMemberRefId().equalsIgnoreCase(m.getId())) {
                                if (!map.containsKey(t.getId())) map.put(t.getId(), t.toAbstractItem(r.getId()));
                            }
                        }
                    }
                }


            } else if (searchText.startsWith("GROUP:")) {

                String[] searchTextArray = searchText.split(":");
                if (searchTextArray.length < 2) return new ArrayList<>(map.values());
                String searchGroup = searchTextArray[1].trim();
                if (r.getGroup().toLowerCase().startsWith(searchGroup)) {
                    if (!map.containsKey(r.getId())) map.put(r.getId(), r.toAbstractItem());
                }

            } else {
                // search all fields
                if (r.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                    if (!map.containsKey(r.getId())) map.put(r.getId(), r.toAbstractItem());
                }
                if (r.getDescription() != null && r.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    // TODO clean description of html code (from text formatting) or search will not work
                    if (!map.containsKey(r.getId())) map.put(r.getId(), r.toAbstractItem());
                }

                // search tasks
                for (Task t : r.getTasks().getList()) {
                    if (t.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(t.getId())) map.put(t.getId(), t.toAbstractItem(r.getId()));
                    }
                    if (t.getDescription() != null && t.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(t.getId())) map.put(t.getId(), t.toAbstractItem(r.getId()));
                    }
                }

                // search issues
                for (Issue i : r.getIssues().getList()) {
                    if (!Utils.isNullOrEmpty(i.getProblem()) && i.getProblem().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r.toAbstractItem());
                    }
                    if (!Utils.isNullOrEmpty(i.getSolution()) && i.getSolution().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r.toAbstractItem());
                    }
                }

                // search comments
                for (Comment c : r.getComments().getList()) {
                    if (!Utils.isNullOrEmpty(c.getDescription()) && c.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                        if (!map.containsKey(r.getId())) map.put(r.getId(), r.toAbstractItem());
                    }
                }
            }

            // search meetings
            for (Meeting m : project.getMeetingHistory().getList()) {
                if (!Utils.isNullOrEmpty(m.getDescription()) && m.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    if (!map.containsKey(m.getId())) map.put(m.getId(), m.toAbstractItem());
                }
            }

            for (Meeting m : project.getMeetingTemplates().getList()) {
                if (!Utils.isNullOrEmpty(m.getDescription()) && m.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    if (!map.containsKey(m.getId())) map.put(m.getId(), m.toAbstractItem());
                }
            }

            // search problems
            for (Problem p : project.getProblems().getList()) {
                if (!Utils.isNullOrEmpty(p.getDescription()) && p.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    if (!map.containsKey(p.getId())) map.put(p.getId(), p.toAbstractItem());
                }
            }

            // search notes
            for (Note n : project.getProjectNotes().getList()) {
                if (!Utils.isNullOrEmpty(n.getDescription()) && n.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    if (!map.containsKey(n.getId())) map.put(n.getId(), n.toAbstractItem());
                }
            }

        }
        return new ArrayList<>(map.values());
    }

    public int getReqListSize() {
        if (searchText == null) return getRequirements().size();
        return getAbstractItems().size();
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
        r.setLastModified(Utils.getXmlGregorianCalendarNow());
        project.addNewRequirement(r);
        logger.info("new req: " + r.getId() + ", title: " + newReqTitle);
        DAO.getInstance().saveProject(project);
        newReqTitle = null;
        project.addHistoryItem(new HistoryItem(r.getTitle(), "/pjm/project/requirement.xhtml?projectId=" + project.getId() + "&reqId=" + r.getId(), "REQ", "New request created"));
        growlInfoMessage("New requirement created");
    }

    public void deleteReqAction(String id) {
        for (Iterator<Requirement> it = project.getRequirements().getList().iterator(); it.hasNext();) {
            Requirement r = it.next();
            if (r.getId().equals(id)) {
                it.remove();
                logger.info("deleteReqAction: req: " + id);
                project.addHistoryItem(new HistoryItem(r.getTitle(), "n/a", "REQ", "Request deleted"));
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
        project.addHistoryItem(new HistoryItem(m.getName() + " " + m.getLastName(), "/pjm/project/members.xhtml?projectId=" + project.getId(), "MEMBER", "New member created"));
        growlInfoMessage("New member joined");
    }

    public void deleteMemberAction(Member member) {
        for (Iterator<Member> it = project.getMembers().getList().iterator(); it.hasNext();) {
            Member m = it.next();
            if (m.getId().equals(member.getId())) {
                it.remove();
                logger.info("member: " + member.getId());
                project.addHistoryItem(new HistoryItem(m.getName() + " " + m.getLastName(), "/pjm/project/members.xhtml?projectId=" + project.getId(), "MEMBER", "Member deleted"));
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


    public List<HistoryItem> getHistoryItems() {
        return project.getHistoryItems();
    }


}
