package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Testbed implements Serializable {

    private static final long serialVersionUID = 178145501549L;

    private String id;
    private String title;
    private List<Server> servers = new ArrayList<>();


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

    public List<Server> getServers() {
        return servers;
    }

    @XmlElement(name = "server")
    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public void addNewServer(Server server) {
        servers.add(server);
    }

}
