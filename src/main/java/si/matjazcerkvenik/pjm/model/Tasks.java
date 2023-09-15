package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tasks implements Serializable {

    private static final long serialVersionUID = 16484961015L;

    private List<Task> list = new ArrayList<>();

    public List<Task> getList() {
        return list;
    }

    @XmlElement(name = "task")
    public void setList(List<Task> list) {
        this.list = list;
    }

    public void addNewTask(Task task) {
        list.add(task);
    }
}
