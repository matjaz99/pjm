package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Service;
import si.matjazcerkvenik.pjm.model.Testbed;
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
public class UiTestbedsBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 21058904530L;

    private static final Logger logger = LoggerFactory.getLogger(UiTestbedsBean.class);

    private Testbed testbed;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        String testbedId = requestParameterMap.getOrDefault("testbedId", null);
        project = uiAppBean.getProject(id);
        for (Testbed tb : project.getTestbeds().getList()) {
            if (tb.getId().equals(testbedId)) testbed = tb;
        }
        logger.info("loaded " + project.getName());
    }

    public Testbed getTestbed() {
        return testbed;
    }

    public void setTestbed(Testbed testbed) {
        this.testbed = testbed;
    }

    private String newTestbedTitle;

    public String getNewTestbedTitle() {
        return newTestbedTitle;
    }

    public void setNewTestbedTitle(String newTestbedTitle) {
        this.newTestbedTitle = newTestbedTitle;
    }

    public void addNewTestbedAction() {
        if (Utils.isNullOrEmpty(newTestbedTitle)) return;
        Testbed tb = new Testbed();
        tb.setId(Utils.getMd5ChecksumShortSalted(newTestbedTitle));
        tb.setTitle(newTestbedTitle);
        project.getTestbeds().addNewTestbed(tb);
        logger.info("new testbed: " + newTestbedTitle);
        DAO.getInstance().saveProject(project);
        newTestbedTitle = null;
        growlInfoMessage("New testbed created");
    }

    public void deleteTestbedAction(String id) {
        for (Iterator<Testbed> it = project.getTestbeds().getList().iterator(); it.hasNext();) {
            Testbed tb = it.next();
            if (tb.getId().equals(id)) {
                it.remove();
                logger.info("deleteTestbedAction: id: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Testbed deleted");
    }





    private String newServiceConnectionString;

    public String getNewServiceConnectionString() {
        return newServiceConnectionString;
    }

    public void setNewServiceConnectionString(String newServiceConnectionString) {
        this.newServiceConnectionString = newServiceConnectionString;
    }

    public void addNewServiceAction() {
        if (Utils.isNullOrEmpty(newServiceConnectionString)) return;
        Service s = new Service();
        s.setId(Utils.getMd5ChecksumShortSalted(newServiceConnectionString));
        s.setConnectionString(newServiceConnectionString);
        testbed.addNewService(s);
        logger.info("new service: " + newServiceConnectionString);
        DAO.getInstance().saveProject(project);
        newServiceConnectionString = null;
        growlInfoMessage("New service created");
    }
}
