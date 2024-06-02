package si.matjazcerkvenik.pjm.web;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.animation.Animation;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.*;
import si.matjazcerkvenik.pjm.util.PjmDateFormat;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class UiStatisticsBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 2104684905L;

    private static final Logger logger = LoggerFactory.getLogger(UiStatisticsBean.class);


    private Project project;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded: " + project.getName());
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getProjectStarted() {
        return Utils.getFormattedTimestamp(project.getProjectStarted(), PjmDateFormat.DATE_SI);
    }

    public String getPlannedEnd() {
        return Utils.getFormattedTimestamp(project.getPlannedEnd(), PjmDateFormat.DATE_SI);
    }

    public String getExpectedEnd() {
        return Utils.getFormattedTimestamp(project.getExpectedEnd(), PjmDateFormat.DATE_SI);
    }

    public int getRequirementsTotalCount() {
        return project.getRequirements().getList().size();
    }

    /**
     * Get number of tasks with the selected status.
     * @param status
     * @return
     */
    public int getTasksTotalCount(String status) {
        int count = 0;
        for (Requirement r : project.getRequirements().getList()) {
            if (status.equalsIgnoreCase("all")) {
                // all tasks regardless of status
                count += r.getTasks().getList().size();
            } else {
                count += r.getTasksByStatus(status).size();
            }
        }
        return count;
    }

    public List<Requirement> getRequirementsWithTaskStatus(String status) {
        return project.getRequirementsByTaskStatus(status);
    }

    public List<Requirement> getRequirementsWithoutTasks() {
        return project.getRequirementsWithoutTasks();
    }

    public List<Requirement> getObsoleteRequirements() {
        return project.getObsoleteRequirements();
    }

    public List<Requirement> getVerifiedRequirements() {
        return project.getRequirements().getList().stream()
                .filter(requirement -> requirement.isVerified())
                .collect(Collectors.toList());
    }

    public int getVerifiedRequirementsPercentage() {
        return (int) (getVerifiedRequirements().size() * 100 / project.getRequirements().getList().size());
    }

    public int getIssuesTotalCount() {
        int count = 0;
        for (Requirement r : project.getRequirements().getList()) {
            count = count + r.getIssues().getList().size();
        }
        return count;
    }

    public int getOpenIssuesPercentage() {
        int count = 0;
        int open = 0;
        for (Requirement r : project.getRequirements().getList()) {
            count = count + r.getIssues().getList().size();
            open = open + r.getOpenIssues().size();
        }
        return open * 100 / count;
    }









    private BarChartModel barModel;

    public BarChartModel getBarModel() {
        createBarModel();
        return barModel;
    }

    public void createBarModel() {
        barModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Tasks by status");

        List<Number> values = new ArrayList<>();
        values.add(getTasksTotalCount(TaskStatus.DRAFT.label));
        values.add(getTasksTotalCount(TaskStatus.IN_PROGRESS.label));
        values.add(getTasksTotalCount(TaskStatus.CLARIFY.label));
        values.add(getTasksTotalCount(TaskStatus.WAITING.label));
        values.add(getTasksTotalCount(TaskStatus.COMPLETE.label));
        // values.add(55);
        // values.add(40);
        barDataSet.setData(values);

        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
//        bgColor.add("rgba(153, 102, 255, 0.2)");
//        bgColor.add("rgba(201, 203, 207, 0.2)");
        barDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
//        borderColor.add("rgb(153, 102, 255)");
//        borderColor.add("rgb(201, 203, 207)");
        barDataSet.setBorderColor(borderColor);
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

        List<String> labels = new ArrayList<>();
        labels.add(TaskStatus.DRAFT.label);
        labels.add(TaskStatus.IN_PROGRESS.label);
        labels.add(TaskStatus.CLARIFY.label);
        labels.add(TaskStatus.WAITING.label);
        labels.add(TaskStatus.COMPLETE.label);
//        labels.add("June");
//        labels.add("July");
        data.setLabels(labels);
        barModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
//        options.setMaintainAspectRatio(false);
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setBeginAtZero(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Bar Chart");
        options.setTitle(title);

        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("italic");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);

        // disable animation
        Animation animation = new Animation();
        animation.setDuration(0);
        options.setAnimation(animation);

        barModel.setOptions(options);
    }

    public int getNumberOfTasksAssignedToMember(String refId) {
        int i = 0;
        for (Requirement r : project.getRequirements().getList()) {
            for (Task t : r.getTasks().getList()) {
                if (t.getAssignee().getMemberRefId() == null && refId.equals("unassigned")) i++;
                if (t.getAssignee().getMemberRefId() == null) continue;
                if (t.getAssignee().getMemberRefId().equals(refId)) i++;
            }
        }
        return i;
    }


}
