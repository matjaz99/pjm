package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Formatter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiPlanningBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 3112455688L;

    private static final Logger logger = LoggerFactory.getLogger(UiPlanningBean.class);

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
    }

    public void saveNotes() {
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Saved");
    }


}
