package si.matjazcerkvenik.pjm.web;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Meeting;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Formatter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiMeetingsBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 34151584621L;

    private static final Logger logger = LoggerFactory.getLogger(UiMeetingsBean.class);

    private Meeting meeting;

    private Date plannedMeetingDate;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        String meetingId = requestParameterMap.getOrDefault("meetingId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
        for (Meeting m : project.getMeetingTemplates().getList()) {
            if (m.getId().equalsIgnoreCase(meetingId)) {
                meeting = m;
                break;
            }
        }
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public String getMeetingTitle() {
        return meeting.getTitle();
    }

    public void setMeetingTitle(String title) {
        meeting.setTitle(title);
        saveProjectModifications("Saved");
    }

    public Date getPlannedMeetingDate() {
        if (meeting.getPlannedDate() != null) {
            plannedMeetingDate = Formatter.gregorianCalendarToDate(meeting.getPlannedDate());
        }
        return plannedMeetingDate;
    }

    public void setPlannedMeetingDate(Date plannedMeetingDate) {
        this.plannedMeetingDate = plannedMeetingDate;
    }

    public void onPlannedMeetingDateSelect(SelectEvent<Date> event) {
        Date d = event.getObject();
        meeting.setPlannedDate(Formatter.dateToGregorianCalendar(plannedMeetingDate));
        saveProjectModifications("Saved");
    }





    private String newMeetingTitle;

    public String getNewMeetingTitle() {
        return newMeetingTitle;
    }

    public void setNewMeetingTitle(String newMeetingTitle) {
        this.newMeetingTitle = newMeetingTitle;
    }

    public void addNewMeetingTemplateAction() {
        if (Formatter.isNullOrEmpty(newMeetingTitle)) return;
        Meeting meeting = new Meeting();
        meeting.setId(Formatter.getMd5ChecksumShortSalted(newMeetingTitle));
        meeting.setTitle(newMeetingTitle);
        project.addNewMeetingTemplate(meeting);
        logger.info("new meeting template: " + newMeetingTitle);
        DAO.getInstance().saveProject(project);
        newMeetingTitle = null;
        growlInfoMessage("New meeting template prepared");
    }

    public void deleteMeetingTemplateAction(String id) {
        for (Iterator<Meeting> it = project.getMeetingTemplates().getList().iterator(); it.hasNext();) {
            Meeting m = it.next();
            if (m.getId().equals(id)) {
                it.remove();
                logger.info("id: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Meeting template deleted");
    }

}
