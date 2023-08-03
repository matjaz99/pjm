package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.DAO;
import si.matjazcerkvenik.pjm.Props;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.util.MD5Checksum;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Iterator;

@ManagedBean
//@SessionScoped
@ViewScoped
public class UiBean implements Serializable {

    private static final long serialVersionUID = 16764183254L;

    private static final Logger logger = LoggerFactory.getLogger(UiBean.class);

    private Project project;

    public Project getProject() {
        if (project == null) {
//            project = DAO.getInstance().loadProject(Props.PJM_PROJECTS_DIRECTORY + "/project_01.xml");
            project = DAO.getInstance().loadProject("project_test.xml");
        }
        return project;
    }

    public String getProjectName() {
        return project.getProjectName();
    }

    public void setProjectName(String s) {
        project.setProjectName(s);
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
        if (Utils.isNullOrEmpty(newReqId)) newReqId = MD5Checksum.getMd5ChecksumShort(newReqTitle);
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
        for (Iterator<Requirement> it = project.getRequirements().getRequirementsList().iterator(); it.hasNext();) {
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
