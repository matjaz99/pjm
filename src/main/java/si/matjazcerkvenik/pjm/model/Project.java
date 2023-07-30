package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serializable;

@XmlRootElement
public class Project implements Serializable {

    private static final long serialVersionUID = 17224348209L;

    private String projectId;
    private String projectName;
    private String projectPath;
    private Requirements requirements;

    public String getProjectId() {
        return projectId;
    }

    @XmlTransient
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    @XmlElement
    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
        if (requirements == null) {
            requirements = new Requirements();
        }
        requirements.addNewRequirement(req);
    }

}
