package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Issue;
import si.matjazcerkvenik.pjm.model.Link;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Formatter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiIssuesBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 282242679104L;

    private static final Logger logger = LoggerFactory.getLogger(UiIssuesBean.class);



    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
    }


    public List<Issue> getOpenIssuesList() {
        List<Issue> list = new ArrayList<>();
        for (Requirement req : project.getRequirements().getList()) {
            for (Issue issue : req.getIssues().getList()) {
                if (!issue.isResolved()) {
                    issue.setReqRefId(req.getId());
                    list.add(issue);
                }
            }
        }
        return list;
    }

    public void resolveIssueAction(String id) {
        for (Requirement req : project.getRequirements().getList()) {
            for (Issue issue : req.getIssues().getList()) {
                if (issue.getId().equals(id)) {
                    issue.setResolved(!issue.isResolved());
                    DAO.getInstance().saveProject(project);
                    growlInfoMessage("Issue resolved");
                    return;
                }
            }
        }
    }

    public Requirement findRequirement(String reqId) {
        for (Requirement r : project.getRequirements().getList()) {
            if (r.getId().equals(reqId)) return r;
        }
        return null;
    }

}
