package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Requirement implements Serializable {

    private static final long serialVersionUID = 17864122354L;

    private String id;
    private String title;
    private String description;
    private XMLGregorianCalendar created;
    private String group = "undefined";
    private String priority = "P3";
    private Link itcmLink = new Link();
    private Link rrcLink = new Link();
    /** Last modified date if description was modified, if comment or issue was opened, if task was created */
    private XMLGregorianCalendar lastModified;
    private Tasks tasks = new Tasks();
    private Comments comments = new Comments();
    private Tags tags = new Tags();
    private Issues issues = new Issues();
    private boolean obsolete = false;
    private String obsoleteReason;

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @XmlElement
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        if (description != null && description.length() > 1000) {
            return description.substring(0, 1000) + " ...";
        }
        return description;
    }

    public XMLGregorianCalendar getCreated() {
        return created;
    }

    @XmlAttribute(name = "created")
    public void setCreated(XMLGregorianCalendar created) {
        this.created = created;
    }

    public XMLGregorianCalendar getLastModified() {
        return lastModified;
    }

    @XmlAttribute(name = "lastModified")
    public void setLastModified(XMLGregorianCalendar lastModified) {
        this.lastModified = lastModified;
    }

    public String getGroup() {
        return group;
    }

    @XmlElement
    public void setGroup(String group) {
        this.group = group;
    }

    public String getPriority() {
        return priority;
    }

    @XmlElement
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Link getItcmLink() {
        return itcmLink;
    }

    @XmlElement
    public void setItcmLink(Link itcmLink) {
        this.itcmLink = itcmLink;
    }

    public Link getRrcLink() {
        return rrcLink;
    }

    @XmlElement
    public void setRrcLink(Link rrcLink) {
        this.rrcLink = rrcLink;
    }

    public Tasks getTasks() {
        return tasks;
    }

    @XmlElement
    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    public void addNewTask(Task taks) {
        tasks.addNewTask(taks);
    }

    public Comments getComments() {
        return comments;
    }

    @XmlElement
    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public void addNewComment(Comment comment) {
        comments.addNewComment(comment);
    }

    public Tags getTags() {
        return tags;
    }

    @XmlElement
    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public Issues getIssues() {
        return issues;
    }

    @XmlElement
    public void setIssues(Issues issues) {
        this.issues = issues;
    }

    public void addNewIssue(Issue issue) {
        issues.addNewIssue(issue);
    }

    public List<Issue> getOpenIssues() {
        List<Issue> list = new ArrayList<>();
        for (Issue issue : issues.getList()) {
            if (!issue.isResolved()) {
                issue.setReqRefId(id);
                list.add(issue);
            }
        }
        return list;
    }

    public boolean isObsolete() {
        return obsolete;
    }

    @XmlElement
    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }

    public String getObsoleteReason() {
        return obsoleteReason;
    }

    @XmlElement
    public void setObsoleteReason(String obsoleteReason) {
        this.obsoleteReason = obsoleteReason;
    }

    /**
     * Calculate percentage of completed tasks on requirement.
     * @return progress in percentage
     */
    public int calculateProgressOnRequirement() {
        if (tasks.getList().size() == 0) return 0;
        int all = tasks.getList().size();
        int complete = 0;
        for (Task t : tasks.getList()) {
            if (t.getStatus() != null && t.getStatus().equalsIgnoreCase(TaskStatus.COMPLETE.label)) complete++;
        }
        return (int) complete * 100 / all;
    }

    /**
     * Get the blue shades of Fade-out star for GUI
     * @param factors
     * @return color
     */
    public String getFadeOutStarColor(int[] factors) {
        if (lastModified == null) return "white";
        int age = Utils.getAgeInDays(lastModified);
        if (age < factors[0]) return "blue";
        if (age < factors[1]) return "dodgerblue"; // lightskyblue
        if (age < factors[2]) return "lightsteelblue";
        if (age < factors[3]) return "#E6F0FF ";
        return "white";
    }
}
