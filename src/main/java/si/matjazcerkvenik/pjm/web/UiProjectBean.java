package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.util.Formatter;
import si.matjazcerkvenik.pjm.util.MD5Checksum;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiProjectBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 22483045884L;

    private static final Logger logger = LoggerFactory.getLogger(UiProjectBean.class);

    @ManagedProperty(value="#{uiAppBean}")
    private UiAppBean uiAppBean;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
    }

    public UiAppBean getUiAppBean() {
        return uiAppBean;
    }

    public void setUiAppBean(UiAppBean uiAppBean) {
        this.uiAppBean = uiAppBean;
    }

    public String getProjectName() {
        return project.getName();
    }

    /**
     * Change the name of the project
     * @param s name
     */
    public void setProjectName(String s) {
        project.setName(s);
        DAO.getInstance().saveProject(project);
    }


    private String newReqTitle;

    public String getNewReqTitle() {
        return newReqTitle;
    }

    public void setNewReqTitle(String newReqTitle) {
        this.newReqTitle = newReqTitle;
    }

    public void addNewReqAction() {
        if (Formatter.isNullOrEmpty(newReqTitle)) return;
        Requirement r = new Requirement();
        r.setId(MD5Checksum.getMd5ChecksumShortSalted(newReqTitle));
        r.setTitle(newReqTitle);
        r.setCreated(Formatter.getXmlGregorianCalendarNow());
        project.addNewRequirement(r);
        logger.info("new req: " + r.getId() + ", title: " + newReqTitle);
        DAO.getInstance().saveProject(project);
        newReqTitle = null;
        growlInfoMessage("New requirement created");
    }

    public void deleteReqAction(String id) {
        for (Iterator<Requirement> it = project.getRequirements().getList().iterator(); it.hasNext();) {
            Requirement r = it.next();
            if (r.getId().equals(id)) {
                it.remove();
                logger.info("deleteReqAction: req: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Requirement deleted");
    }

}
