package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {

    private static final long serialVersionUID = 1611228713055L;

    private String id;
    private String title;
    private String description;
    private String status;
    private Assignee assignee = new Assignee();
    private XMLGregorianCalendar created;
    private XMLGregorianCalendar lastModified;
    private int estimatedEffort;

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

    public String getStatus() {
        return status;
    }

    @XmlElement
    public void setStatus(String status) {
        this.status = status;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    @XmlElement
    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public void assignTo(String memberName) {

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

    public int getEstimatedEffort() {
        return estimatedEffort;
    }

    @XmlElement(name = "estimatedEffort")
    public void setEstimatedEffort(int estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }

    /**
     * Return abstract representation of this object.
     * @return ai
     */
    public AbstractItem toAbstractItem(String reqId) {
        AbstractItem ai = new AbstractItem();
        ai.setTitle(this.title);
        ai.setItemType("task");
        ai.setText(this.description);
        ai.setUrl("/project/task?tskId=" + this.id + "&reqId=" + reqId);
        return ai;
    }

    public List<Hashtag> getHashtags(String reqId) {
        List<Hashtag> htList = new ArrayList<>();
        List<String> list = Utils.findHashtags(description);
        for (String s : list) {
            Hashtag ht = new Hashtag(s, "/project/task?tskId=" + this.id + "&reqId=" + reqId);
            htList.add(ht);
        }
        return htList;
    }

}
