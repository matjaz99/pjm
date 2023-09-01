package si.matjazcerkvenik.pjm.web;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class UiBean {

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

}
