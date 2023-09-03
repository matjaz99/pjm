package si.matjazcerkvenik.pjm.web;

import org.primefaces.component.colorpicker.ColorPicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.*;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.MD5Checksum;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiTagsBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 23119748541L;

    private static final Logger logger = LoggerFactory.getLogger(UiTagsBean.class);

    @ManagedProperty(value="#{uiAppBean}")
    private UiAppBean uiAppBean;

    private Project project;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("UiTagsBean:init: loaded " + project.getName());
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

    public List<Tag> getTagDefinitions() {
        if (project.getTagDefinitions() == null) return new ArrayList<>();
        return project.getTagDefinitions().getList();
    }


    private String newTagTitle;
    private String newTagColor = "#faf200";

    public String getNewTagTitle() {
        return newTagTitle;
    }

    public void setNewTagTitle(String newTagTitle) {
        this.newTagTitle = newTagTitle;
    }

    public String getNewTagColor() {
        return newTagColor;
    }

    public void setNewTagColor(String newTagColor) {
        this.newTagColor = newTagColor;
    }

    public void addNewTagAction() {
        if (Utils.isNullOrEmpty(newTagTitle)) return;
        if (!newTagColor.startsWith("#")) newTagColor = "#" + newTagColor;
        Tag t = new Tag();
        t.setId(MD5Checksum.getMd5ChecksumShortSalted(newTagTitle));
        t.setName(newTagTitle);
        t.setColor(newTagColor);
        project.addNewTag(t);
        logger.info("addNewTagAction: tag: " + t.getId() + ", title: " + newTagTitle + ", color: " + newTagColor);
        DAO.getInstance().saveProject(project);
        newTagTitle = null;
        newTagColor = "#faf200";
        growlInfoMessage("New tag created");
    }

    public void deleteTagAction(String id) {
        for (Iterator<Tag> it = project.getTagDefinitions().getList().iterator(); it.hasNext();) {
            Tag r = it.next();
            if (r.getId().equals(id)) {
                it.remove();
                logger.info("deleteTagAction: tag: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Tag deleted");
    }

    public void onColorChange(AjaxBehaviorEvent e) {
        ColorPicker picker = (ColorPicker) e.getComponent();
        growlInfoMessage("Color changed");
    }

    public void saveChanges() {
        DAO.getInstance().saveProject(project);
    }


    
}
