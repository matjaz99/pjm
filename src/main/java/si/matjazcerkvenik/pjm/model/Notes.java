package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Notes implements Serializable {

    private static final long serialVersionUID = 3518848451L;

    private List<Note> list = new ArrayList<>();

    public List<Note> getList() {
        return list;
    }

    @XmlElement(name = "note")
    public void setList(List<Note> list) {
        this.list = list;
    }

    public void addNewNote(Note note) {
        list.add(note);
    }
}
