package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

public class Requirements {

    private List<Requirement> requirementsList;

    public List<Requirement> getRequirementsList() {
        return requirementsList;
    }

    @XmlElement(name = "requirement")
    public void setRequirementsList(List<Requirement> requirementsList) {
        this.requirementsList = requirementsList;
    }

    public void addNewRequirement(Requirement req) {
        if (requirementsList == null) requirementsList = new ArrayList<>();
        requirementsList.add(req);
    }
}
