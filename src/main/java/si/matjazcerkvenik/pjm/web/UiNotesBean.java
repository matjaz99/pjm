package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Link;
import si.matjazcerkvenik.pjm.model.Note;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiNotesBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 28824868716L;

    private static final Logger logger = LoggerFactory.getLogger(UiNotesBean.class);



    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
    }



    private String newNoteDesc;
    private String newNoteTitle;

    public String getNewNoteDesc() {
        return newNoteDesc;
    }

    public void setNewNoteDesc(String newNoteDesc) {
        this.newNoteDesc = newNoteDesc;
    }

    public String getNewNoteTitle() {
        return newNoteTitle;
    }

    public void setNewNoteTitle(String newNoteTitle) {
        this.newNoteTitle = newNoteTitle;
    }

    public void addNewNoteAction() {
        if (Utils.isNullOrEmpty(newNoteDesc)) return;
        if (Utils.isNullOrEmpty(newNoteTitle)) return;
        Note note = new Note();
        note.setId(Utils.getMd5ChecksumShortSalted(newNoteDesc));
        note.setDescription(newNoteDesc);
        note.setTitle(newNoteTitle);
        project.getProjectNotes().addNewNote(note);
        logger.info("new note: " + newNoteTitle);
        DAO.getInstance().saveProject(project);
        newNoteDesc = null;
        newNoteTitle = null;
        growlInfoMessage("New note created");
    }

    public void deleteNoteAction(String id) {
        for (Iterator<Note> it = project.getProjectNotes().getList().iterator(); it.hasNext();) {
            Note l = it.next();
            if (l.getId().equals(id)) {
                it.remove();
                logger.info("deleteNoteAction: id: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Note deleted");
    }

}
