package si.matjazcerkvenik.pjm.web;

import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Tag;
import si.matjazcerkvenik.pjm.util.DAO;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

public class UiBean {

    @ManagedProperty(value="#{uiAppBean}")
    protected UiAppBean uiAppBean;

    protected Project project;

    public UiAppBean getUiAppBean() {
        return uiAppBean;
    }

    public void setUiAppBean(UiAppBean uiAppBean) {
        this.uiAppBean = uiAppBean;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Something was changed. Just save to disk.
     */
    public void saveProjectModifications(String message) {
        DAO.getInstance().saveProject(project);
        growlInfoMessage(message);
    }

    public void growlInfoMessage(String s) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s, null));
    }

    public void growlErrorMessage(String s) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, s, null));
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
