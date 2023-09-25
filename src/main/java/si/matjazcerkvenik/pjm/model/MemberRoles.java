package si.matjazcerkvenik.pjm.model;

public enum MemberRoles {
    DEVELOPER("Developer"),
    VERIFICATION("Verification"),
    PJM("Project manager"),
    PDM("Product manager"),
    HEAD("Head"),
    OUTSOURCING("Outsourcing");

    public final String label;

    private MemberRoles(String label) {
        this.label = label;
    }
}
