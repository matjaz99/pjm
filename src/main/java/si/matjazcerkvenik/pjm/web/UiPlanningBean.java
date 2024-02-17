package si.matjazcerkvenik.pjm.web;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Member;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiPlanningBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 3112455688L;

    private static final Logger logger = LoggerFactory.getLogger(UiPlanningBean.class);

    private Date startedDate;
    private Date plannedEndDate;
    private Date expectedEndDate;

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

    public Date getStartedDate() {
        if (project.getProjectStarted() != null) {
            startedDate = Utils.gregorianCalendarToDate(project.getProjectStarted());
        }
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public void onStartedDateSelect(SelectEvent<Date> event) {
        Date d = event.getObject();
        project.setProjectStarted(Utils.dateToGregorianCalendar(startedDate));
        saveProjectModifications("Saved");
    }

    public Date getPlannedEndDate() {
        if (project.getPlannedEnd() != null) {
            plannedEndDate = Utils.gregorianCalendarToDate(project.getPlannedEnd());
        }
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public void onPlannedEndDateSelect(SelectEvent<Date> event) {
        Date d = event.getObject();
        project.setPlannedEnd(Utils.dateToGregorianCalendar(plannedEndDate));
        saveProjectModifications("Saved");
    }

    public Date getExpectedEndDate() {
        if (project.getExpectedEnd() != null) {
            expectedEndDate = Utils.gregorianCalendarToDate(project.getExpectedEnd());
        }
        return expectedEndDate;
    }

    public void setExpectedEndDate(Date expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public void onExpectedEndDateSelect(SelectEvent<Date> event) {
        Date d = event.getObject();
        project.setExpectedEnd(Utils.dateToGregorianCalendar(expectedEndDate));
        saveProjectModifications("Saved");
    }

    public void projectStateChangeEvent() {
        //project.setState();
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Project promoted to " + project.getState());
    }
}
