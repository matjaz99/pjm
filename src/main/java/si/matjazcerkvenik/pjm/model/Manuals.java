package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Manuals implements Serializable {

    private static final long serialVersionUID = 3481184911L;

    private List<Manual> list = new ArrayList<>();

    public List<Manual> getList() {
        return list;
    }

    @XmlElement(name = "manual")
    public void setList(List<Manual> list) {
        this.list = list;
    }

    public void addNewManual(Manual manual) {
        list.add(manual);
    }
}
