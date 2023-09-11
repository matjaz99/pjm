package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tags implements Serializable {

    private static final long serialVersionUID = 278118847105L;

    private List<Tag> list = new ArrayList<>();

    public List<Tag> getList() {
        return list;
    }

    @XmlElement(name = "tag")
    public void setList(List<Tag> list) {
        this.list = list;
    }

    public void addNewTag(Tag tag) {
        list.add(tag);
    }
}
