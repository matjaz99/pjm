package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meeting implements Serializable {

    private static final long serialVersionUID = 28741561251L;

    private String id;
    private String title;
    private String description;
    private XMLGregorianCalendar plannedDate;
    private boolean concluded = false;

    public String getId() {
        return id;
    }

    @XmlElement
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

    public XMLGregorianCalendar getPlannedDate() {
        return plannedDate;
    }

    @XmlAttribute
    public void setPlannedDate(XMLGregorianCalendar plannedDate) {
        this.plannedDate = plannedDate;
    }

    public boolean isConcluded() {
        return concluded;
    }

    @XmlAttribute
    public void setConcluded(boolean concluded) {
        this.concluded = concluded;
    }
}
