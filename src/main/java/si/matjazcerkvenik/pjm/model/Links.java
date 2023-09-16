package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Links implements Serializable {

    private static final long serialVersionUID = 3214449521L;

    private List<Link> list = new ArrayList<>();

    public List<Link> getList() {
        return list;
    }

    @XmlElement(name = "link")
    public void setList(List<Link> list) {
        this.list = list;
    }

    public void addNewLink(Link link) {
        list.add(link);
    }
}
