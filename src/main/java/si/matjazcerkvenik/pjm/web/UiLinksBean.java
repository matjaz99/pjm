package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Link;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Formatter;
import si.matjazcerkvenik.pjm.util.MD5Checksum;

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
public class UiLinksBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 26544808411L;

    private static final Logger logger = LoggerFactory.getLogger(UiStatisticsBean.class);

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



    private String newLinkDesc;
    private String newLink;

    public String getNewLinkDesc() {
        return newLinkDesc;
    }

    public void setNewLinkDesc(String newLinkDesc) {
        this.newLinkDesc = newLinkDesc;
    }

    public String getNewLink() {
        return newLink;
    }

    public void setNewLink(String newLink) {
        this.newLink = newLink;
    }

    public void addNewLinkAction() {
        if (Formatter.isNullOrEmpty(newLinkDesc)) return;
        if (Formatter.isNullOrEmpty(newLink)) return;
        Link link = new Link();
        link.setId(MD5Checksum.getMd5ChecksumShortSalted(newLinkDesc));
        link.setDescription(newLinkDesc);
        link.setLink(newLink);
        project.addNewLink(link);
        logger.info("new link: " + newLink);
        DAO.getInstance().saveProject(project);
        newLinkDesc = null;
        newLink = null;
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
