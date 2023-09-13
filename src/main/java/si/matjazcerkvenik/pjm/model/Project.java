package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

@XmlRootElement
public class Project implements Serializable {

    private static final long serialVersionUID = 17224348208L;

    private String id;
    private String name;
    private XMLGregorianCalendar created;
    private String projectPath;
    private Requirements requirements;
    private Tags tagDefinitions;
    private Links links = new Links();

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

    public XMLGregorianCalendar getCreated() {
        return created;
    }

    @XmlAttribute(name = "created")
    public void setCreated(XMLGregorianCalendar created) {
        this.created = created;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getProjectPath() {
        return projectPath;
    }

    @XmlTransient
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    @XmlElement
    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }

    public void addNewRequirement(Requirement req) {
        if (requirements == null) requirements = new Requirements();
        requirements.addNewRequirement(req);
    }

    public Tags getTagDefinitions() {
        return tagDefinitions;
    }

    @XmlElement(name = "tagDefs")
    public void setTagDefinitions(Tags tagDefinitions) {
        this.tagDefinitions = tagDefinitions;
    }

    public void addNewTag(Tag req) {
        if (tagDefinitions == null) tagDefinitions = new Tags();
        tagDefinitions.addNewTag(req);
    }

    public Links getLinks() {
        return links;
    }

    @XmlElement(name = "links")
    public void setLinks(Links links) {
        this.links = this.links;
    }

    public void addNewLink(Link link) {
        links.addNewLink(link);
    }

}
