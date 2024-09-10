package si.matjazcerkvenik.pjm.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Link;
import si.matjazcerkvenik.pjm.model.Problem;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class UiProblemsBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 48815430481L;

    private static final Logger logger = LoggerFactory.getLogger(UiProblemsBean.class);

    private Problem problem;
    private boolean hideSolved;



    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        String problemId = requestParameterMap.getOrDefault("problemId", null);
        project = uiAppBean.getProject(id);
        for (Problem p : project.getProblems().getList()) {
            if (p.getId().equals(problemId)) problem = p;
        }
        logger.info("loaded: " + project.getName());
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public boolean isHideSolved() {
        return hideSolved;
    }

    public void setHideSolved(boolean hideSolved) {
        this.hideSolved = hideSolved;
    }

    public List<Problem> getFilteredProblems() {
        if (hideSolved) {
            return project.getUnsolvedProblems();
        } else {
            return project.getProblems().getList();
        }
    }


    // NEW PROBLEM

    private String newProblemTitle;

    public String getNewProblemTitle() {
        return newProblemTitle;
    }

    public void setNewProblemTitle(String newProblemTitle) {
        this.newProblemTitle = newProblemTitle;
    }

    public void addNewProblemAction() {
        if (Utils.isNullOrEmpty(newProblemTitle)) return;
        Problem problem = new Problem();
        problem.setId(Utils.getMd5ChecksumShortSalted(newProblemTitle));
        problem.setCreated(Utils.getXmlGregorianCalendarNow());
        problem.setLastModified(Utils.getXmlGregorianCalendarNow());
        problem.setTitle(newProblemTitle);
        project.addNewProblem(problem);
        logger.info("new problem: " + newProblemTitle);
        DAO.getInstance().saveProject(project);
        newProblemTitle = null;
        growlInfoMessage("New problem created");
    }

    public void deleteProblemAction(String id) {
        for (Iterator<Problem> it = project.getProblems().getList().iterator(); it.hasNext();) {
            Problem p = it.next();
            if (p.getId().equals(id)) {
                it.remove();
                logger.info("deleteProblemAction: id: " + id);
                break;
            }
        }
        DAO.getInstance().saveProject(project);
        growlInfoMessage("Problem deleted");
    }

}
