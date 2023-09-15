package si.matjazcerkvenik.pjm.model;

public class Alarms {

    // info, warning, danger

    public static Alarm reqsWithoutTasks = new Alarm("reqsWithoutTasks", "Requirements without tasks", "Make sure every requirement has at least one task.", "warning");
    public static Alarm reqsWithWaitingTasks = new Alarm("reqsWithWaitingTasks", "Tasks are waiting", "Some tasks might be blocking the project.", "danger");
    public static Alarm reqsWithClarifyTasks = new Alarm("reqsWithClarifyTasks", "Clarification needed", "Additional explanation is needed.", "warning");

}
