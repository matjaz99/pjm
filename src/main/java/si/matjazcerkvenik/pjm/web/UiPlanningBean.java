package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiPlanningBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 3112455688L;

    private static final Logger logger = LoggerFactory.getLogger(UiPlanningBean.class);

    @ManagedProperty(value="#{uiAppBean}")
    private UiAppBean uiAppBean;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("initialized " + project.getName());
    }

    public UiAppBean getUiAppBean() {
        return uiAppBean;
    }

    public void setUiAppBean(UiAppBean uiAppBean) {
        this.uiAppBean = uiAppBean;
    }


}
