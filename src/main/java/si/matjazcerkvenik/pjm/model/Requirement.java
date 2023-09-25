package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

public class Requirement implements Serializable {

    private static final long serialVersionUID = 17864122354L;

    private String id;
    private String title;
    private String description;
    private XMLGregorianCalendar created;
    private Tasks tasks = new Tasks();
    private Comments comments = new Comments();;
    private Tags tags = new Tags();
    private Issues issues = new Issues();

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

    public int calculateProgressOnRequirement() {
        if (tasks.getList().size() == 0) return 0;
        int all = tasks.getList().size();
        int complete = 0;
        for (Task t : tasks.getList()) {
            if (t.getStatus() != null && t.getStatus().equalsIgnoreCase(TaskStatus.COMPLETE.label)) complete++;
        }
        return (int) complete * 100 / all;
    }
}
