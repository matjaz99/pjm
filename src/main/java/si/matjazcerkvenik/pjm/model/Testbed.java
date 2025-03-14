package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Testbed implements Serializable {

    private static final long serialVersionUID = 178145501549L;

    private String id;
    private String title;
    private List<Service> services = new ArrayList<>();


    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @XmlElement
    public void setTitle(String title) {
        this.title = title;
    }

    public List<Service> getServices() {
        return services;
    }

    @XmlElement(name = "service")
    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void addNewService(Service server) {
        services.add(server);
    }

}
