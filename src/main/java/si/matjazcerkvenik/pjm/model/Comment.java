package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import si.matjazcerkvenik.pjm.util.PjmDateFormat;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

public class Comment implements Serializable {

    private static final long serialVersionUID = 24887164804L;

    private String id;
    private XMLGregorianCalendar created;
    private XMLGregorianCalendar lastModified;
    private String description;

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    @XmlValue
    public void setDescription(String description) {
        this.description = description;
    }



    // other methods

    public String getLastModifiedDateOnly() {
        return Utils.getFormattedTimestamp(lastModified, PjmDateFormat.DATE_SI);
    }
}
