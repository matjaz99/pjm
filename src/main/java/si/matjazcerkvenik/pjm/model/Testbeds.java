package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Testbeds implements Serializable {

    private static final long serialVersionUID = 1614815120L;

    private List<Testbed> list = new ArrayList<>();

    public List<Testbed> getList() {
        return list;
    }

    @XmlElement(name = "testbed")
    public void setList(List<Testbed> list) {
        this.list = list;
    }

    public void addNewTestbed(Testbed testbed) {
        list.add(testbed);
    }
}
