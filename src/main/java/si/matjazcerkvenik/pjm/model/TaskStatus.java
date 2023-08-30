package si.matjazcerkvenik.pjm.model;

public enum TaskStatus {
    DRAFT("Draft"),
    IN_PROGRESS("In progress"),
    CLARIFY("Clarify"),
    WAITING("Waiting"),
    COMPLETE("Complete");

    public final String label;

    private TaskStatus(String label) {
        this.label = label;
    }
}
