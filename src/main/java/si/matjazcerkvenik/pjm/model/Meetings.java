package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meetings implements Serializable {

    private static final long serialVersionUID = 22114843354L;

    private List<Meeting> list = new ArrayList<>();

    public List<Meeting> getList() {
        return list;
    }

    @XmlElement(name = "meeting")
    public void setList(List<Meeting> list) {
        this.list = list;
    }

    public void addNewMeeting(Meeting meeting) {
        list.add(meeting);
    }
}
