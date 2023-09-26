package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class Project implements Serializable {

    private static final long serialVersionUID = 17224348208L;

    private String id;
    private String name;
    private XMLGregorianCalendar created;
    private XMLGregorianCalendar projectStarted;
    /** This is initially planned end date. It should not change. Delay on the project will be calculated from this. */
    private XMLGregorianCalendar plannedEnd;
    /** This is expected end date. It can be delayed according to progress on the project. */
    private XMLGregorianCalendar expectedEnd;
    private XMLGregorianCalendar predictedEnd; // TODO
    private String projectPath;
    private Requirements requirements = new Requirements();
    private Tags tagDefinitions = new Tags();
    private Links links = new Links();
    private String notes;
    private Members members = new Members();
    private Map<String, Alarm> activeAlarms = new HashMap<>();

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public XMLGregorianCalendar getCreated() {
        return created;
    }

    @XmlAttribute(name = "created")
    public void setCreated(XMLGregorianCalendar created) {
        this.created = created;
    }

    public XMLGregorianCalendar getProjectStarted() {
        return projectStarted;
    }

    @XmlElement
    public void setProjectStarted(XMLGregorianCalendar projectStarted) {
        this.projectStarted = projectStarted;
    }

    public XMLGregorianCalendar getPlannedEnd() {
        return plannedEnd;
    }

    @XmlElement
    public void setPlannedEnd(XMLGregorianCalendar plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    public XMLGregorianCalendar getExpectedEnd() {
        return expectedEnd;
    }

    @XmlElement
    public void setExpectedEnd(XMLGregorianCalendar expectedEnd) {
        this.expectedEnd = expectedEnd;
    }

    public String getProjectPath() {
        return projectPath;
    }

    @XmlTransient
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    @XmlElement
    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }

    public void addNewRequirement(Requirement req) {
        requirements.addNewRequirement(req);
    }

    public Tags getTagDefinitions() {
        return tagDefinitions;
    }

    @XmlElement(name = "tagDefs")
    public void setTagDefinitions(Tags tagDefinitions) {
        this.tagDefinitions = tagDefinitions;
    }

    public void addNewTag(Tag req) {
        tagDefinitions.addNewTag(req);
    }

    public Links getLinks() {
        return links;
    }

    @XmlElement(name = "links")
    public void setLinks(Links links) {
        this.links = links;
    }

    public void addNewLink(Link link) {
        links.addNewLink(link);
    }

    public String getNotes() {
        return notes;
    }

    @XmlElement(name = "notes")
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Members getMembers() {
        return members;
    }

    @XmlElement(name = "members")
    public void setMembers(Members members) {
        this.members = members;
    }

    public void addNewMember(Member member) {
        members.addNewMember(member);
    }

    public Map<String, Alarm> getActiveAlarms() {
        return activeAlarms;
    }

    @XmlTransient
    public void setActiveAlarms(Map<String, Alarm> activeAlarms) {
        this.activeAlarms = activeAlarms;
    }

    public void raiseAlarm(Alarm a) {
        if (!activeAlarms.containsKey(a.getId())) activeAlarms.put(a.getId(), a);
    }

    public List<Issue> getOpenIssues() {
        List<Issue> list = new ArrayList<>();
        for (Requirement req : requirements.getList()) {
            list.addAll(req.getOpenIssues());
        }
        return list;
    }

    public List<Requirement> getRequirementsByTaskStatus(String status) {
        List<Requirement> list = new ArrayList<>();
        for (Requirement r : requirements.getList()) {
            if (r.getTasks() != null && r.getTasks().getList() != null) {
                for (Task t : r.getTasks().getList()) {
                    if (t.getStatus() != null && t.getStatus().equalsIgnoreCase(status)) list.add(r);
                }
            }
        }
        return list;
    }

    /**
     * This method works slightly different that <code>Requirement.calculateProgressOnRequirement</code>
     * because it takes into account also requirements without tasks. In this case it is assumed that
     * requirement is not fulfilled and counts as 1 incomplete task.
     * @return progress in percentage
     */
    public int calculateProgressOnProject() {
        int all = 0;
        int complete = 0;
        if (requirements.getList().size() == 0) return 0;
        for (Requirement r : requirements.getList()) {
            if (r.getTasks().getList().size() == 0) {
                all++;
            } else {
                all = all + r.getTasks().getList().size();
                for (Task t : r.getTasks().getList()) {
                    if (t.getStatus() != null && t.getStatus().equalsIgnoreCase(TaskStatus.COMPLETE.label)) complete++;
                }
            }
        }
        return (int) complete * 100 / all;
    }

}
