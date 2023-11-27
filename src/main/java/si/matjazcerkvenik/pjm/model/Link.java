package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;

public class Link implements Serializable {

    private static final long serialVersionUID = 306215849L;

    private String id;
    private String description;
    private String href;

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        if (href == null || href.length() == 0) href = "-";
        return href;
    }

    @XmlElement
    public void setHref(String href) {
        this.href = href;
    }
}
