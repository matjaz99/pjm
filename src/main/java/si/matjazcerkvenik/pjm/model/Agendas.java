package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Agendas implements Serializable {

    private static final long serialVersionUID = 3431050864L;

    private List<Agenda> list = new ArrayList<>();

    public List<Agenda> getList() {
        return list;
    }

    @XmlElement(name = "agenda")
    public void setList(List<Agenda> list) {
        this.list = list;
    }

    public void addNewAgenda(Agenda agenda) {
        list.add(agenda);
    }
}
