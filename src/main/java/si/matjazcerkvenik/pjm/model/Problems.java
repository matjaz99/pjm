package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Problems implements Serializable {

    private static final long serialVersionUID = 22483111560L;

    private List<Problem> list = new ArrayList<>();

    public List<Problem> getList() {
        return list;
    }

    @XmlElement(name = "problem")
    public void setList(List<Problem> list) {
        this.list = list;
    }

    public void addNewProblem(Problem problem) {
        list.add(problem);
    }
}
