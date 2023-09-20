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
    private String projectPath;
    private Requirements requirements = new Requirements();
    private Tags tagDefinitions = new Tags();
    private Links links = new Links();
    private String notes;
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

    public XMLGregorianCalendar getCreated() {
        return created;
    }

    @XmlAttribute(name = "created")
    public void setCreated(XMLGregorianCalendar created) {
        this.created = created;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
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

    public List<Issue> getAllOpenIssues() {
        List<Issue> list = new ArrayList<>();
        for (Requirement req : requirements.getList()) {
            for (Issue issue : req.getIssues().getList()) {
                if (!issue.isResolved()) {
                    issue.setReqRefId(req.getId());
                    list.add(issue);
                }
            }
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

}
