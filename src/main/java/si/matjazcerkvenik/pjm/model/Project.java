package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import si.matjazcerkvenik.pjm.util.Props;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@XmlRootElement
public class Project implements Serializable {

    private static final long serialVersionUID = 17224348208L;
    public static final String PROJECT_STATE_PLANNING = "Planning";
    public static final String PROJECT_STATE_ONHOLD = "On-hold";
    public static final String PROJECT_STATE_ACTIVE = "Active";
    public static final String PROJECT_STATE_COMPLETED = "Completed";
    public static final String PROJECT_STATE_HIDDEN = "Hidden";

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
    private String state;
    private Requirements requirements = new Requirements();
    private Tags tagDefinitions = new Tags();
    private Links links = new Links();
    /** These notes are shown in planning view */
    private String notes;
    private Notes projectNotes = new Notes();
    private Members members = new Members();
    private Checklist planningChecklist = new Checklist();
    private Meetings meetingTemplates = new Meetings();
    private Meetings meetingHistory = new Meetings();
    private Map<String, Alarm> activeAlarms = new HashMap<>();

    private LinkedList<HistoryItem> historyItems = new LinkedList<>();

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

    public String getState() {
        return state;
    }

    @XmlElement
    public void setState(String state) {
        this.state = state;
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

    public Notes getProjectNotes() {
        return projectNotes;
    }

    @XmlElement(name = "projectNotes")
    public void setProjectNotes(Notes projectNotes) {
        this.projectNotes = projectNotes;
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

    public Checklist getPlanningChecklist() {
        return planningChecklist;
    }

    @XmlElement(name = "planningChecklist")
    public void setPlanningChecklist(Checklist planningChecklist) {
        this.planningChecklist = planningChecklist;
    }

    public Meetings getMeetingTemplates() {
        return meetingTemplates;
    }

    @XmlElement(name = "meetingTemplates")
    public void setMeetingTemplates(Meetings meetingTemplates) {
        this.meetingTemplates = meetingTemplates;
    }

    public void addNewMeetingTemplate(Meeting meeting) {
        meetingTemplates.addNewMeeting(meeting);
    }

    /**
     * Search for meeting in templates and in history.
     * @param id
     * @return meeting
     */
    public Meeting findMeeting(String id) {
        Meeting meeting = null;
        for (Meeting m : meetingTemplates.getList()) {
            if (m.getId().equalsIgnoreCase(id)) return m;
        }
        for (Meeting m : meetingHistory.getList()) {
            if (m.getId().equalsIgnoreCase(id)) return m;
        }
        return null;
    }

    /**
     * Delete meeting template.
     * @param id
     * @return meeting that was deleted
     */
    public Meeting deleteMeetingTemplate(String id) {
        for (Iterator<Meeting> it = meetingTemplates.getList().iterator(); it.hasNext();) {
            Meeting m = it.next();
            if (m.getId().equals(id)) {
                it.remove();
                return m;
            }
        }
        return null;
    }

    public Meetings getMeetingHistory() {
        return meetingHistory;
    }

    @XmlElement(name = "meetingHistory")
    public void setMeetingHistory(Meetings meetingHistory) {
        this.meetingHistory = meetingHistory;
    }

    public void addMeetingToHistory(Meeting meeting) {
        meetingHistory.addNewMeeting(meeting);
    }

    public Meeting deleteMeetingFromHistory(String id) {
        for (Iterator<Meeting> it = meetingHistory.getList().iterator(); it.hasNext();) {
            Meeting m = it.next();
            if (m.getId().equals(id)) {
                it.remove();
                return m;
            }
        }
        return null;
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
            if (r.getTasks().getList() != null) {
                for (Task t : r.getTasks().getList()) {
                    if (t.getStatus() != null && t.getStatus().equalsIgnoreCase(status)) list.add(r);
                }
            }
        }
        return list;
    }

    public List<Requirement> getRequirementsWithoutTasks() {
        return requirements.getList().stream()
                .filter(req -> (req.getTasks().getList().size() == 0))
                .collect(Collectors.toList());
    }

    public List<Requirement> getObsoleteRequirements() {
        return requirements.getList().stream()
                .filter(req -> (req.isObsolete()))
                .collect(Collectors.toList());
    }

    /**
     * This method takes into account also requirements without tasks. In such case it is assumed that
     * requirement is not fulfilled and counts as 1 incomplete task. Obsolete requests are ignored in the
     * calculation.
     * @return progress in percentage
     */
    public int calculateProgressOnProject() {
        int all = 0;
        int complete = 0;
        if (requirements.getList().size() == 0) return 0;
        for (Requirement r : requirements.getList()) {
            if (!r.isObsolete()) {
                if (r.getTasks().getList().size() == 0) {
                    all++;
                } else {
                    all = all + r.getTasks().getList().size();
                    for (Task t : r.getTasks().getList()) {
                        if (!Utils.isNullOrEmpty(t.getStatus())
                                && t.getStatus().equalsIgnoreCase(TaskStatus.COMPLETE.label)) complete++;
                    }
                }
            }
        }
        return (int) complete * 100 / all;
    }











    // MANAGING HISTORY

    public void addHistoryItem(HistoryItem historyItem) {
        if (historyItems.size() >= Props.LAST_HISTORY_ITEMS_SIZE) historyItems.removeFirst();
        historyItems.addLast(historyItem);
    }

    public List<HistoryItem> getHistoryItems() {
        return historyItems;
    }


}
