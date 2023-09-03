package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlValue;

import java.io.Serializable;

public class Tag implements Serializable {

    private static final long serialVersionUID = 15537411854L;

    private String id;
    /** Id of tag definition */
    private String refId;
    private String name;
    private String color;
    private boolean selected = true;

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    @XmlAttribute
    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getName() {
        return name;
    }

    @XmlValue
    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    @XmlAttribute
    public void setColor(String color) {
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    @XmlTransient
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return name;
    }
}
