package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.DAO;
import si.matjazcerkvenik.pjm.model.Requirement;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
@RequestScoped
public class UiRequirementBean implements Serializable {

    private static final long serialVersionUID = 27734903592L;

    private static final Logger logger = LoggerFactory.getLogger(UiRequirementBean.class);

    @ManagedProperty(value="#{uiBean}")
    private UiBean uiBean;
    
    private Requirement requirement;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("id", null);
        for (Requirement r :
                uiBean.getProject().getRequirements().getRequirementsList()) {
            if (r.getId().equals(id)) {
                requirement = r;
                break;
            }
        }

    }

    public UiBean getUiBean() {
        return uiBean;
    }

    public void setUiBean(UiBean uiBean) {
        this.uiBean = uiBean;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public void saveDescription() {
        logger.info("saveDescription: " + requirement.getDescription());
    }

}
