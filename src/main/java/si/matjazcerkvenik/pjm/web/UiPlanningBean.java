package si.matjazcerkvenik.pjm.web;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Checklist;
import si.matjazcerkvenik.pjm.model.ChecklistItem;
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
@SuppressWarnings("unused")
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

    public void favoriteChangeEvent() {
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Project marked as favorite: " + project.isFavorite());
    }







    // Checklist

    private boolean npiOpened;
    private boolean rdProjectOpened;
    private boolean risksDefined;
    private boolean requirementsDefined;
    private boolean rrcCollectionReady;
    private boolean effortEstimated;
    private boolean productReleaseOpened;
    private boolean b100Reached;
    private boolean b600Reached;
    private boolean gitlabProjectOpened;
    private boolean primaveraHours;
    private boolean reqsGenerated;
    private boolean sbbOpened;

    public boolean isNpiOpened() {
        npiOpened = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_FP_NPI_OPENED).isChecked();
        return npiOpened;
    }

    public void setNpiOpened(boolean npiOpened) {
        this.npiOpened = npiOpened;
    }

    public boolean isB100Reached() {
        b100Reached = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_FP_B100_REACHED).isChecked();
        return b100Reached;
    }

    public void setB100Reached(boolean b100Reached) {
        this.b100Reached = b100Reached;
    }

    public boolean isRdProjectOpened() {
        rdProjectOpened = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_FP_RD_PROJECT_OPENED).isChecked();
        return rdProjectOpened;
    }

    public void setRdProjectOpened(boolean rdProjectOpened) {
        this.rdProjectOpened = rdProjectOpened;
    }

    public boolean isRequirementsDefined() {
        requirementsDefined = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_RRC_REQUIREMENTS_DEFINED).isChecked();
        return requirementsDefined;
    }

    public void setRequirementsDefined(boolean requirementsDefined) {
        this.requirementsDefined = requirementsDefined;
    }

    public boolean isRrcCollectionReady() {
        rrcCollectionReady = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_RRC_COLLECTION_READY).isChecked();
        return rrcCollectionReady;
    }

    public void setRrcCollectionReady(boolean rrcCollectionReady) {
        this.rrcCollectionReady = rrcCollectionReady;
    }

    public boolean isEffortEstimated() {
        effortEstimated = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_EFFORT_ESTIMATED).isChecked();
        return effortEstimated;
    }

    public void setEffortEstimated(boolean effortEstimated) {
        this.effortEstimated = effortEstimated;
    }

    public boolean isProductReleaseOpened() {
        productReleaseOpened = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_ITCM_PRODUCT_RELEASE).isChecked();
        return productReleaseOpened;
    }

    public void setProductReleaseOpened(boolean productReleaseOpened) {
        this.productReleaseOpened = productReleaseOpened;
    }

    public boolean isRisksDefined() {
        risksDefined = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_FP_RISKS_DEFINED).isChecked();
        return risksDefined;
    }

    public void setRisksDefined(boolean risksDefined) {
        this.risksDefined = risksDefined;
    }

    public boolean isB600Reached() {
        b600Reached = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_FP_B600_REACHED).isChecked();
        return b600Reached;
    }

    public void setB600Reached(boolean b600Reached) {
        this.b600Reached = b600Reached;
    }

    public boolean isGitlabProjectOpened() {
        gitlabProjectOpened = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_GITLAB_PROJECT_OPENED).isChecked();
        return gitlabProjectOpened;
    }

    public void setGitlabProjectOpened(boolean gitlabProjectOpened) {
        this.gitlabProjectOpened = gitlabProjectOpened;
    }

    public boolean isPrimaveraHours() {
        primaveraHours = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_ITCM_PRIMAVERA_HOURS).isChecked();
        return primaveraHours;
    }

    public void setPrimaveraHours(boolean primaveraHours) {
        this.primaveraHours = primaveraHours;
    }

    public boolean isReqsGenerated() {
        reqsGenerated = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_ITCM_REQS_GENERATED).isChecked();
        return reqsGenerated;
    }

    public void setReqsGenerated(boolean reqsGenerated) {
        this.reqsGenerated = reqsGenerated;
    }

    public boolean isSbbOpened() {
        sbbOpened = project.getPlanningChecklist().getChecklistItem(Checklist.ITEM_ITSIM_SBB_OPENED).isChecked();
        return sbbOpened;
    }

    public void setSbbOpened(boolean sbbOpened) {
        this.sbbOpened = sbbOpened;
    }

    public void checklistItemChecked(String item) {

        ChecklistItem checklistItem = project.getPlanningChecklist().getChecklistItem(item);

        if (item.equals(Checklist.ITEM_FP_NPI_OPENED)) {
            checklistItem.setChecked(npiOpened);
        } else if (item.equals(Checklist.ITEM_FP_RD_PROJECT_OPENED)) {
            checklistItem.setChecked(rdProjectOpened);
        } else if (item.equals(Checklist.ITEM_FP_RISKS_DEFINED)) {
            checklistItem.setChecked(risksDefined);
        } else if (item.equals(Checklist.ITEM_FP_B100_REACHED)) {
            checklistItem.setChecked(b100Reached);
        } else if (item.equals(Checklist.ITEM_FP_B600_REACHED)) {
            checklistItem.setChecked(b600Reached);
        } else if (item.equals(Checklist.ITEM_RRC_REQUIREMENTS_DEFINED)) {
            checklistItem.setChecked(requirementsDefined);
        } else if (item.equals(Checklist.ITEM_RRC_COLLECTION_READY)) {
            checklistItem.setChecked(rrcCollectionReady);
        } else if (item.equals(Checklist.ITEM_ITCM_REQS_GENERATED)) {
            checklistItem.setChecked(reqsGenerated);
        } else if (item.equals(Checklist.ITEM_ITCM_PRODUCT_RELEASE)) {
            checklistItem.setChecked(productReleaseOpened);
        } else if (item.equals(Checklist.ITEM_ITCM_PRIMAVERA_HOURS)) {
            checklistItem.setChecked(primaveraHours);
        } else if (item.equals(Checklist.ITEM_EFFORT_ESTIMATED)) {
            checklistItem.setChecked(effortEstimated);
        } else if (item.equals(Checklist.ITEM_GITLAB_PROJECT_OPENED)) {
            checklistItem.setChecked(gitlabProjectOpened);
        } else if (item.equals(Checklist.ITEM_ITSIM_SBB_OPENED)) {
            checklistItem.setChecked(sbbOpened);
        }

        checklistItem.setCheckedDate(Utils.getXmlGregorianCalendarNow());
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Checked");

    }
}
