package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

public class Task implements Serializable {

    private static final long serialVersionUID = 1611228713055L;

    private String id;
    private String title;
    private String description;
    private String status;
    private XMLGregorianCalendar created;
    private XMLGregorianCalendar lastModified;

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
}
