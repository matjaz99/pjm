package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;

public class Requirement implements Serializable {

    private static final long serialVersionUID = 17864122354L;

    private String id;
    private String title;
    private String description;
    private Tasks tasks;
    private Comments comments;

    private Tags tags = new Tags();

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

    public Tasks getTasks() {
        return tasks;
    }

    @XmlElement
    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    public void addNewTask(Task taks) {
        if (tasks == null) tasks = new Tasks();
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
        if (comments == null) comments = new Comments();
        comments.addNewComment(comment);
    }

    public Tags getTags() {
        return tags;
    }

    @XmlElement
    public void setTags(Tags tags) {
        this.tags = tags;
    }
}
