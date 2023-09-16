package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Link;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Formatter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiLinksBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 26544808411L;

    private static final Logger logger = LoggerFactory.getLogger(UiLinksBean.class);



    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
    }



    private String newLinkDesc;
    private String newLinkHref;

    public String getNewLinkDesc() {
        return newLinkDesc;
    }

    public void setNewLinkDesc(String newLinkDesc) {
        this.newLinkDesc = newLinkDesc;
    }

    public String getNewLinkHref() {
        return newLinkHref;
    }

    public void setNewLinkHref(String newLinkHref) {
        this.newLinkHref = newLinkHref;
    }

    public void addNewLinkAction() {
        if (Formatter.isNullOrEmpty(newLinkDesc)) return;
        if (Formatter.isNullOrEmpty(newLinkHref)) return;
        Link link = new Link();
        link.setId(Formatter.getMd5ChecksumShortSalted(newLinkDesc));
        link.setDescription(newLinkDesc);
        link.setHref(newLinkHref);
        project.addNewLink(link);
        logger.info("new link: " + newLinkHref);
        DAO.getInstance().saveProject(project);
        newLinkDesc = null;
        newLinkHref = null;
        growlInfoMessage("New link created");
    }

    public void deleteLinkAction(String id) {
        for (Iterator<Link> it = project.getLinks().getList().iterator(); it.hasNext();) {
            Link l = it.next();
            if (l.getId().equals(id)) {
                it.remove();
                logger.info("deleteLinkAction: id: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Link deleted");
    }

}
