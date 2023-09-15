package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Requirements implements Serializable {

    private static final long serialVersionUID = 1711749401L;

    private List<Requirement> list = new ArrayList<>();

    public List<Requirement> getList() {
        return list;
    }

    @XmlElement(name = "requirement")
    public void setList(List<Requirement> list) {
        this.list = list;
    }

    public void addNewRequirement(Requirement req) {
        list.add(req);
    }
}
