package si.matjazcerkvenik.pjm.web;

import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Tag;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class UiBean {

    protected Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void growlInfoMessage(String s) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s, null));
    }

    public String convertStatusToSeverity(String status) {
        if (status.equalsIgnoreCase("Draft")) {
            return "primary";
        } else if (status.equalsIgnoreCase("In progress")) {
            return "info";
        } else if (status.equalsIgnoreCase("Clarify")) {
            return "warning";
        } else if (status.equalsIgnoreCase("Waiting")) {
            return "danger";
        } else if (status.equalsIgnoreCase("Complete")) {
            return "success";
        }
        return "primary";
    }

    public Tag tag2TagDef(Tag tag) {
        for (Tag tagDef : project.getTagDefinitions().getList()) {
            if (tag.getRefId().equalsIgnoreCase(tagDef.getId())) {
                return tagDef;
            }
        }
        return null;
    }

}