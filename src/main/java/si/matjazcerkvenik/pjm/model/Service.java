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
    private boolean icmpStatus;
    private boolean portStatus;


    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
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

        if (Utils.isNullOrEmpty(schema)) schema = "http";
        if (Utils.isNullOrEmpty(username)) username = "";
        if (Utils.isNullOrEmpty(password)) password = "";
        if (Utils.isNullOrEmpty(hostname)) hostname = "unknown-hostname";
        if (Utils.isNullOrEmpty(context)) context = "";
        if (Utils.isNullOrEmpty(port)) {
            if (schema.equalsIgnoreCase("http")) port = "80";
            if (schema.equalsIgnoreCase("https")) port = "443";
            if (schema.equalsIgnoreCase("jdbc:mysql")) port = "3306";
            if (schema.equalsIgnoreCase("jdbc:postgres")) port = "5432";
        }

//        String[] a1 = connectionString.split("://");
//        schema = a1[0];
//        if (a1[1].contains("@")) {
//            String[] a2 = a1[1].split("@");
//            if (a2[0].contains(":")) {
//                String[] a3 = a2[0].split(":");
//                username = a3[0];
//                password = a3[1];
//            } else {
//                username = a2[0];
//            }
//            if (a2[1].contains(":")) {
//                String[] a4 = a2[1].split(":");
//                hostname = a4[0];
//                if (a4[1].contains("/")) {
//                    String[] a5 = a4[1].split("/");
//                    port = Integer.parseInt(a5[0]);
//                    context = "/" + a5[1];
//                } else {
//                    port = Integer.parseInt(a4[1]);
//                }
//            } else {
//                // TODO
//                hostname = a2[1];
//            }
//        } else {
//            if (a1[1].contains(":")) {
//                String[] a4 = a1[1].split(":");
//                hostname = a4[0];
//                if (a4[1].contains("/")) {
//                    String[] a5 = a4[1].split("/");
//                    port = Integer.parseInt(a5[0]);
//                } else {
//                    port = Integer.parseInt(a4[1]);
//                }
//            } else {
//                hostname = a1[1];
//                if (schema.equalsIgnoreCase("http")) port = 80;
//                if (schema.equalsIgnoreCase("https")) port = 443;
//            }
//        }
    }

    public String getGeneratedConnectionString() {
        String connStr = schema + "://";
        return schema + "://" + username + (Utils.isNullOrEmpty(username) ? "" : ":") + password + (Utils.isNullOrEmpty(username) ? "" : "@") + hostname + ":" + port + "/" + context;
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
