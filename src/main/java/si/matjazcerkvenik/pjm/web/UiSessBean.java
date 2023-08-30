package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Requirement;
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
//@SessionScoped
@ViewScoped
public class UiSessBean implements Serializable {

    private static final long serialVersionUID = 22483045884L;

    private static final Logger logger = LoggerFactory.getLogger(UiSessBean.class);

    @ManagedProperty(value="#{uiAppBean}")
    private UiAppBean uiAppBean;

    private Project project;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("UiSessBean:init loaded " + project.getName());
    }

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


    private String newReqId;
    private String newReqTitle;

    public String getNewReqId() {
        return newReqId;
    }

    public void setNewReqId(String newReqId) {
        this.newReqId = newReqId;
    }

    public String getNewReqTitle() {
        return newReqTitle;
    }

    public void setNewReqTitle(String newReqTitle) {
        this.newReqTitle = newReqTitle;
    }

    public void addNewReqAction() {
        logger.info("addNewReqAction: new req id: " + newReqId + ", title: " + newReqTitle);
        if (Utils.isNullOrEmpty(newReqTitle)) return;
        if (Utils.isNullOrEmpty(newReqId)) newReqId = MD5Checksum.getMd5ChecksumShortSalted(newReqTitle);
        Requirement r = new Requirement();
        r.setId(newReqId);
        r.setTitle(newReqTitle);
        project.addNewRequirement(r);
        logger.info("addNewReqAction: new req: " + newReqId);
        DAO.getInstance().saveProject(project);
        newReqId = null;
        newReqTitle = null;
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
    }

}
