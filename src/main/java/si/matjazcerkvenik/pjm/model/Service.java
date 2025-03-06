package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import si.matjazcerkvenik.pjm.util.Utils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service implements Serializable {

    private static final long serialVersionUID = 7113640251L;

    private String id;
    private String description;
    private String connectionString;
    private String schema;
    private String username;
    private String password;
    private String hostname;
    private String port;
    private String context;
    private String ipAddress;
    private boolean icmpStatus;
    private boolean portStatus;
    private boolean monitoringActive = false;


    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public boolean isMonitoringActive() {
        return monitoringActive;
    }

    @XmlAttribute(name = "monitor")
    public void setMonitoringActive(boolean monitoringActive) {
        this.monitoringActive = monitoringActive;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public String getConnectionString() {
        return connectionString;
    }

    @XmlElement
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;

        String regex = "^(?<schema>[a-zA-Z]+)://(?:(?<username>[^:@/]+)(?::(?<password>[^@/]*))?@)?(?<hostname>[^:/?]+)(?::(?<port>\\d+))?(?:/(?<path>[^?]*))?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(connectionString);

        if (matcher.find()) {
            schema = matcher.group("schema");
            username = matcher.group("username");
            password = matcher.group("password");
            hostname = matcher.group("hostname");
            port = matcher.group("port");
            context = matcher.group("path");
        } else {
            System.out.println("Invalid connection string");
        }

        if (Utils.isNullOrEmpty(schema)) schema = "";
        if (Utils.isNullOrEmpty(username)) username = "";
        if (Utils.isNullOrEmpty(password)) password = "";
        if (Utils.isNullOrEmpty(hostname)) hostname = "unknown-hostname";
        if (Utils.isNullOrEmpty(port)) port = "";
        if (Utils.isNullOrEmpty(context)) context = "";
        if (Utils.isNullOrEmpty(port)) {
            if (schema.equalsIgnoreCase("http")) port = "80";
            if (schema.equalsIgnoreCase("https")) port = "443";
            if (schema.equalsIgnoreCase("ssh")) port = "22";
            if (schema.equalsIgnoreCase("jdbc:mysql")) port = "3306";
            if (schema.equalsIgnoreCase("jdbc:postgres")) port = "5432";
        }

    }

    public String getGeneratedConnectionString() {
        if (Utils.isNullOrEmpty(schema)) return hostname;
        return schema + "://" + username + (Utils.isNullOrEmpty(username) ? "" : ":") + password + (Utils.isNullOrEmpty(username) ? "" : "@") + hostname + (Utils.isNullOrEmpty(port) ? "" : ":") + port + "/" + context;
    }

    public String getSchema() {
        return schema;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public String getContext() {
        return context;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @XmlTransient
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isIcmpStatus() {
        return icmpStatus;
    }

    @XmlTransient
    public void setIcmpStatus(boolean icmpStatus) {
        this.icmpStatus = icmpStatus;
    }

    public boolean isPortStatus() {
        return portStatus;
    }

    @XmlTransient
    public void setPortStatus(boolean portStatus) {
        this.portStatus = portStatus;
    }
}
