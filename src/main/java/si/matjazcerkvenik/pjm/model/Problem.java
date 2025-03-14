package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Problem implements Serializable {

    private static final long serialVersionUID = 1664842054L;

    private String id;
    private String title;
    private String description;
    private String status;
    private int priority = 3;
    private boolean solved;
    private XMLGregorianCalendar created;
    private XMLGregorianCalendar lastModified;
    private XMLGregorianCalendar solvedDate;

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

    public int getPriority() {
        return priority;
    }

    @XmlElement
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isSolved() {
        return solved;
    }

    @XmlElement
    public void setSolved(boolean solved) {
        this.solved = solved;
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

    public XMLGregorianCalendar getSolvedDate() {
        return solvedDate;
    }

    @XmlElement(name = "solvedDate")
    public void setSolvedDate(XMLGregorianCalendar solvedDate) {
        this.solvedDate = solvedDate;
    }

    /**
     * Return abstract representation of this object.
     * @return ai
     */
    public AbstractItem toAbstractItem() {
        AbstractItem ai = new AbstractItem();
        ai.setTitle(this.title);
        ai.setItemType("problem");
        ai.setText(this.description);
        ai.setUrl("/project/problem?problemId=" + this.id);
        return ai;
    }

    public List<Hashtag> getHashtags() {
        List<Hashtag> htList = new ArrayList<>();
        List<String> list = Utils.findHashtags(this.description);
        for (String s : list) {
            Hashtag ht = new Hashtag(s, title, "/project/problem?problemId=" + this.id);
            htList.add(ht);
        }
        return htList;
    }

}
