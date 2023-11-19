package si.matjazcerkvenik.pjm.model;

import java.io.Serializable;

public class Alarm implements Serializable {

    private static final long serialVersionUID = 39421548483L;

    private String id;
    private String name;
    private String addInfo;
    private String severity;
    private String href;

    public Alarm() {
    }

    public Alarm(String id, String name, String addInfo, String severity) {
        this.id = id;
        this.name = name;
        this.addInfo = addInfo;
        this.severity = severity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
