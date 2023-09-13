package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Alarm;
import si.matjazcerkvenik.pjm.util.DAO;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiAlarmsBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 2477412501L;

    private static final Logger logger = LoggerFactory.getLogger(UiAlarmsBean.class);


    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
    }



    public List<Alarm> getAlarmList() {
        return DAO.getInstance().getAlarmList(project.getId());
    }

    public String getBadgeAlarmsCount() {
        int i = DAO.getInstance().getAlarmList(project.getId()).size();
        if (i < 10) return Integer.toString(i);
        return "10+";
    }

}
