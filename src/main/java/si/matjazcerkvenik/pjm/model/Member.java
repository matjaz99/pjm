package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

public class Member implements Serializable {

    private static final long serialVersionUID = 3116131487L;

    private String id;
    private String name;
    private String role;

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    @XmlElement
    public void setRole(String role) {
        this.role = role;
    }
}
