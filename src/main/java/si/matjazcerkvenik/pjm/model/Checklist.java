package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Checklist implements Serializable {

    private static final long serialVersionUID = 21648449442L;

    public static final String ITEM_FP_NPI_OPENED = "NPI opened";
    public static final String ITEM_FP_RD_PROJECT_OPENED = "R&D project opened";
    public static final String ITEM_FP_RISKS_DEFINED = "Risks defined";
    public static final String ITEM_FP_B100_REACHED = "B100";
    public static final String ITEM_FP_B600_REACHED = "B600";
    public static final String ITEM_RRC_REQUIREMENTS_DEFINED = "Requirements defined";
    public static final String ITEM_RRC_COLLECTION_READY = "RRC collection ready";
    public static final String ITEM_ITCM_REQS_GENERATED = "REQs generated";
    public static final String ITEM_ITCM_PRODUCT_RELEASE = "PR opened";
    public static final String ITEM_ITCM_PRIMAVERA_HOURS = "PV hours";
    public static final String ITEM_EFFORT_ESTIMATED = "Effort estimated";
    public static final String ITEM_GITLAB_PROJECT_OPENED = "Gitlab project opened";
    public static final String ITEM_ITSIM_SBB_OPENED = "SBB opened";


    public static final String ITEM_B600_REPORT = "B600 report";
//        p.getPlanningChecklist().addNewChecklistItem(new ChecklistItem("Milestones defined"));
//        p.getPlanningChecklist().addNewChecklistItem(new ChecklistItem("Team members defined"));
//        p.getPlanningChecklist().addNewChecklistItem(new ChecklistItem("ITCM project opened"));
//        p.getPlanningChecklist().addNewChecklistItem(new ChecklistItem("Group reservations ready"));

    private List<ChecklistItem> list = new ArrayList<>();

    public List<ChecklistItem> getList() {
        return list;
    }

    @XmlElement(name = "item")
    public void setList(List<ChecklistItem> list) {
        this.list = list;
    }

    public void addNewChecklistItem(ChecklistItem item) {
        for (ChecklistItem i : list) {
            if (!i.getName().equalsIgnoreCase(item.getName())) list.add(item);
        }
    }

    public ChecklistItem getChecklistItem(String name) {
        for (ChecklistItem i : list) {
            if (i.getName().equalsIgnoreCase(name)) return i;
        }
        // create new if not found
        ChecklistItem ci = new ChecklistItem(name);
        list.add(ci);
        return ci;
    }
}
