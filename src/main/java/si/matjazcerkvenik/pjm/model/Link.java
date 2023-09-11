package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;

public class Link implements Serializable {

    private static final long serialVersionUID = 306458499L;

    private String id;
    private String description;
    private String link;

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

    public String getLink() {
        return link;
    }

    @XmlElement
    public void setLink(String link) {
        this.link = link;
    }
}
