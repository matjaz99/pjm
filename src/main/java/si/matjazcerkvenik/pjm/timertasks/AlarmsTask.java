package si.matjazcerkvenik.pjm.timertasks;

import si.matjazcerkvenik.pjm.model.*;
import si.matjazcerkvenik.pjm.util.Utils;

import java.util.List;
import java.util.TimerTask;

public class AlarmsTask extends TimerTask {

    private List<Project> projects;

    public AlarmsTask(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public void run() {

        boolean openedIssues = false;

        for (Project project : projects) {

            project.getActiveAlarms().clear();

            // check if all requirements have tasks
            List<Requirement> reqsNoTask = project.getRequirementsWithoutTasks();
            if (reqsNoTask.size() > 0) {
                Alarm a = new Alarm();
                a.setName("There are requirements without tasks");
                a.setAddInfo("Make sure every requirement has at least one task.");
                a.setSeverity("info");
                a.setId(Utils.getMd5ChecksumShort(a.getName()));
                project.raiseAlarm(a);
            }

            // check requirements with waiting tasks for more than N days
            for (Requirement r : project.getRequirements().getList()) {
                for (Task t : r.getTasks().getList()) {
                    if (t.getStatus().equalsIgnoreCase(TaskStatus.WAITING.label)
                            && Utils.getAgeInDays(t.getLastModified()) > 7) {
                        Alarm a = new Alarm();
                        a.setName("Task is waiting for more than " + Utils.getAgeInDays(t.getLastModified()) + " days");
                        a.setAddInfo(t.getTitle() + " @ " + r.getTitle());
                        a.setSeverity("danger");
                        a.setHref("/project/task.xhtml?projectId=" + project.getId() + "&reqId=" + r.getId() + "&tskId=" + t.getId());
                        a.setId(Utils.getMd5ChecksumShort(a.getName() + r.getTitle()));
                        project.raiseAlarm(a);
                    }
                }
            }

            // check requirements without estimated effort
            for (Requirement r : project.getRequirements().getList()) {
                if (r.getRequirementTotalEstimatedEffort() == 0 && !r.isObsolete()) {
                    Alarm a = new Alarm();
                    a.setName("No estimated effort");
                    a.setAddInfo("Estimate effort");
                    a.setSeverity("warning");
                    a.setHref("/project/requirement.xhtml?projectId=" + project.getId() + "&reqId=" + r.getId());
                    a.setId(Utils.getMd5ChecksumShort(a.getName() + r.getTitle()));
                    project.raiseAlarm(a);
                }
            }

            // check requirements with clarify tasks
            for (Requirement r : project.getRequirements().getList()) {
                for (Task t : r.getTasks().getList()) {
                    if (t.getStatus().equalsIgnoreCase(TaskStatus.CLARIFY.label)) {
                        Alarm a = new Alarm();
                        a.setName("Clarification is needed");
                        a.setAddInfo(t.getTitle() + " @ " + r.getTitle());
                        a.setSeverity("warning");
                        a.setHref("/project/task.xhtml?projectId=" + project.getId() + "&reqId=" + r.getId() + "&tskId=" + t.getId());
                        a.setId(Utils.getMd5ChecksumShort(a.getName() + r.getTitle()));
                        project.raiseAlarm(a);
                    }
                }
            }

            // check if there are any open issues
            if (project.getOpenIssues().size() > 0) {
                Alarm a = new Alarm();
                a.setName("There are opened issues!");
                a.setAddInfo("Resolve the issues");
                a.setSeverity("danger");
                a.setHref("/project/issues.xhtml?projectId=" + project.getId());
                a.setId(Utils.getMd5ChecksumShort(a.getName()));
                project.raiseAlarm(a);
            }
        }


    }
}
