package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Issues implements Serializable {

    private static final long serialVersionUID = 28947161588L;

    private List<Issue> list = new ArrayList<>();

    public List<Issue> getList() {
        return list;
    }

    @XmlElement(name = "issue")
    public void setList(List<Issue> list) {
        this.list = list;
    }

    public void addNewIssue(Issue issue) {
        if (list == null) list = new ArrayList<>();
        list.add(issue);
    }
}
