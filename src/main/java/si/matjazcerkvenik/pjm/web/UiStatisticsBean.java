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
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.model.Task;
import si.matjazcerkvenik.pjm.model.TaskStatus;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiStatisticsBean implements Serializable {

    private static final long serialVersionUID = 2104684905L;

    private static final Logger logger = LoggerFactory.getLogger(UiStatisticsBean.class);

    @ManagedProperty(value="#{uiAppBean}")
    private UiAppBean uiAppBean;

    private Project project;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("UiStatisticsBean:init: loaded " + project.getName());
    }

    public UiAppBean getUiAppBean() {
        return uiAppBean;
    }

    public void setUiAppBean(UiAppBean uiAppBean) {
        this.uiAppBean = uiAppBean;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getRequirementsTotalCount() {
        if (project.getRequirements().getList() == null) return 0;
        return project.getRequirements().getList().size();
    }

    public int getTasksTotalCount(String status) {
        if (project.getRequirements().getList() == null) return 0;
        int count = 0;
        for (Requirement r : project.getRequirements().getList()) {
            if (r.getTasks() == null || r.getTasks().getList() == null) {
                continue;
            } else {
                if (status.equalsIgnoreCase("null")) {
                    count += r.getTasks().getList().size();
                    continue;
                }
                for (Task t : r.getTasks().getList()) {
                    if (t.getStatus() != null && t.getStatus().equalsIgnoreCase(status)) count += 1;
                }
            }
        }
        return count;
    }

    public List<Requirement> getRequirementsWithTaskStatus(String status) {
        return Utils.getRequirementsWithTaskStatus(project, status);
    }

    public List<Requirement> getRequirementsWithoutTasks() {
        return Utils.getRequirementsWithoutTasks(project);
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

}
