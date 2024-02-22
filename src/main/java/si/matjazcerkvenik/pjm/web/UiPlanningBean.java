package si.matjazcerkvenik.pjm.web;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Checklist;
import si.matjazcerkvenik.pjm.model.ChecklistItem;
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



    // Checklist

    private boolean npiOpened;
    private boolean rdProjectOpened;
    private boolean requirementsDefined;
    private boolean rrcCollectionReady;
    private boolean effortEstimated;
    private boolean b100Reached;

    public boolean isNpiOpened() {
        npiOpened = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_NPI_OPENED).isChecked();
        return npiOpened;
    }

    public void setNpiOpened(boolean npiOpened) {
        this.npiOpened = npiOpened;
    }

    public void npiOpenedValueChange() {
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_NPI_OPENED).setChecked(npiOpened);
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_NPI_OPENED).setCheckedDate(Utils.getXmlGregorianCalendarNow());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Checked");
    }

    public boolean isB100Reached() {
        b100Reached = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_B100_REACHED).isChecked();
        return b100Reached;
    }

    public void setB100Reached(boolean b100Reached) {
        this.b100Reached = b100Reached;
    }

    public void b100ReachedValueChange() {
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_B100_REACHED).setChecked(b100Reached);
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_B100_REACHED).setCheckedDate(Utils.getXmlGregorianCalendarNow());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Checked");
    }

    public boolean isRdProjectOpened() {
        rdProjectOpened = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_RD_PROJECT_OPENED).isChecked();
        return rdProjectOpened;
    }

    public void setRdProjectOpened(boolean rdProjectOpened) {
        this.rdProjectOpened = rdProjectOpened;
    }

    public void rdProjectOpenedValueChange() {
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_RD_PROJECT_OPENED).setChecked(rdProjectOpened);
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_RD_PROJECT_OPENED).setCheckedDate(Utils.getXmlGregorianCalendarNow());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Checked");
    }

    public boolean isRequirementsDefined() {
        requirementsDefined = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_REQUIREMENTS_DEFINED).isChecked();
        return requirementsDefined;
    }

    public void setRequirementsDefined(boolean requirementsDefined) {
        this.requirementsDefined = requirementsDefined;
    }

    public void requirementsDefinedValueChange() {
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_REQUIREMENTS_DEFINED).setChecked(requirementsDefined);
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_REQUIREMENTS_DEFINED).setCheckedDate(Utils.getXmlGregorianCalendarNow());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Checked");
    }

    public boolean isRrcCollectionReady() {
        rrcCollectionReady = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_RRC_COLLECTION_READY).isChecked();
        return rrcCollectionReady;
    }

    public void setRrcCollectionReady(boolean rrcCollectionReady) {
        this.rrcCollectionReady = rrcCollectionReady;
    }

    public void rrcCollectionReadyValueChange() {
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_RRC_COLLECTION_READY).setChecked(rrcCollectionReady);
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_RRC_COLLECTION_READY).setCheckedDate(Utils.getXmlGregorianCalendarNow());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Checked");
    }

    public boolean isEffortEstimated() {
        effortEstimated = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_EFFORT_ESTIMATED).isChecked();
        return effortEstimated;
    }

    public void setEffortEstimated(boolean effortEstimated) {
        this.effortEstimated = effortEstimated;
    }

    public void effortEstimatedValueChange() {
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_EFFORT_ESTIMATED).setChecked(effortEstimated);
        project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_EFFORT_ESTIMATED).setCheckedDate(Utils.getXmlGregorianCalendarNow());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Checked");
    }
}
