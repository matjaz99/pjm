package si.matjazcerkvenik.pjm.timertasks;

import si.matjazcerkvenik.pjm.model.Alarms;
import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.model.TaskStatus;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Utils;

import java.util.List;
import java.util.TimerTask;

public class PeriodicTask extends TimerTask {

    private List<Project> projects;

    public PeriodicTask(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public void run() {

        boolean openedIssues = false;

        for (Project project : projects) {

            // check if all requirements have tasks
            List<Requirement> reqsNoTask = Utils.getRequirementsWithoutTasks(project);
            if (reqsNoTask.size() > 0) {
                DAO.getInstance().raiseAlarm(project.getId(), Alarms.reqsWithoutTasks);
            } else {
                DAO.getInstance().clearAlarm(project.getId(), Alarms.reqsWithoutTasks);
            }

            // check requirements with waiting tasks
            List<Requirement> reqsWithWaitingTasks = Utils.getRequirementsWithTaskStatus(project, TaskStatus.WAITING.label);
            if (reqsWithWaitingTasks.size() > 0) {
                DAO.getInstance().raiseAlarm(project.getId(), Alarms.reqsWithWaitingTasks);
            } else {
                DAO.getInstance().clearAlarm(project.getId(), Alarms.reqsWithWaitingTasks);
            }

            // check requirements with clarify tasks
            List<Requirement> reqsWithClarifyTasks = Utils.getRequirementsWithTaskStatus(project, TaskStatus.CLARIFY.label);
            if (reqsWithClarifyTasks.size() > 0) {
                DAO.getInstance().raiseAlarm(project.getId(), Alarms.reqsWithClarifyTasks);
            } else {
                DAO.getInstance().clearAlarm(project.getId(), Alarms.reqsWithClarifyTasks);
            }

            // check if there are opened issues
        }


    }
}
