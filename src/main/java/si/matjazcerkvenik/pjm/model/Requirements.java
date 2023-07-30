package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

public class Requirements {

    private List<Requirement> requirements;

    public List<Requirement> getRequirements() {
        return requirements;
    }

    @XmlElement(name = "requirement")
    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    public void addNewRequirement(Requirement req) {
        if (requirements == null) requirements = new ArrayList<>();
        requirements.add(req);
    }
}
