package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

public class Issue implements Serializable {

    private static final long serialVersionUID = 3166484104L;

    private String id;
    private String solution;
    private String problem;
    private boolean resolved = false;
    private XMLGregorianCalendar created;

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getSolution() {
        return solution;
    }

    @XmlElement
    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getProblem() {
        return problem;
    }

    @XmlElement
    public void setProblem(String problem) {
        this.problem = problem;
    }

    public boolean isResolved() {
        return resolved;
    }

    @XmlElement
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public XMLGregorianCalendar getCreated() {
        return created;
    }

    @XmlAttribute(name = "created")
    public void setCreated(XMLGregorianCalendar created) {
        this.created = created;
    }
}
