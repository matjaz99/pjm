package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Manual;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiManualsBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 4098056447L;

    private static final Logger logger = LoggerFactory.getLogger(UiManualsBean.class);



    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
    }


    public List<Manual> getAllManuals() {
        List<Manual> list = new ArrayList<>();
        for (Project p : uiAppBean.getProjects()) {
            list.addAll(p.getManuals().getList());
        }
        return list;
    }



    private String newManualTitle;

    public String getNewManualTitle() {
        return newManualTitle;
    }

    public void setNewManualTitle(String newManualTitle) {
        this.newManualTitle = newManualTitle;
    }

    public void addNewManualAction() {
        if (Utils.isNullOrEmpty(newManualTitle)) return;
        Manual man = new Manual();
        man.setId(Utils.getMd5ChecksumShortSalted(newManualTitle));
        man.setTitle(newManualTitle);
        man.setRefProjectName(project.getName());
        project.getManuals().addNewManual(man);
        logger.info("new manual: " + newManualTitle);
        DAO.getInstance().saveProject(project);
        newManualTitle = null;
        growlInfoMessage("New manual created");
    }

    public void deleteManualAction(String id) {
        for (Iterator<Manual> it = project.getManuals().getList().iterator(); it.hasNext();) {
            Manual m = it.next();
            if (m.getId().equals(id)) {
                it.remove();
                logger.info("deleteManualAction: id: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Manual deleted");
    }

}
