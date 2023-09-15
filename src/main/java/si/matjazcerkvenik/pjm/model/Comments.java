package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comments implements Serializable {

    private static final long serialVersionUID = 26250947005L;

    private List<Comment> list = new ArrayList<>();

    public List<Comment> getList() {
        return list;
    }

    @XmlElement(name = "comment")
    public void setList(List<Comment> list) {
        this.list = list;
    }

    public void addNewComment(Comment remark) {
        list.add(remark);
    }
}
