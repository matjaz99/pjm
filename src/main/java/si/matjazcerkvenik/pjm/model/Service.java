package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;

public class Service implements Serializable {

    private static final long serialVersionUID = 7113640251L;

    private String id;
    private String connectionString;
    private String schema;
    private String username;
    private String password;
    private String hostname;
    private int port;


    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getConnectionString() {
        return connectionString;
    }

    @XmlElement
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;

        String[] a1 = connectionString.split("://");
        schema = a1[0];
        if (a1[1].contains("@")) {
            String[] a2 = a1[1].split("@");
            if (a2[0].contains(":")) {
                String[] a3 = a2[0].split(":");
                username = a3[0];
                password = a3[1];
            }
            if (a2[1].contains(":")) {
                String[] a4 = a2[1].split(":");
                hostname = a4[0];
                if (a4[1].contains("/")) {
                    String[] a5 = a4[1].split("/");
                    port = Integer.parseInt(a5[0]);
                } else {
                    port = Integer.parseInt(a4[1]);
                }
            } else {
                // TODO
                hostname = a2[1];
            }
        } else {
            if (a1[1].contains(":")) {
                String[] a4 = a1[1].split(":");
                hostname = a4[0];
                if (a4[1].contains("/")) {
                    String[] a5 = a4[1].split("/");
                    port = Integer.parseInt(a5[0]);
                } else {
                    port = Integer.parseInt(a4[1]);
                }
            } else {
                hostname = a1[1];
                if (schema.equalsIgnoreCase("http")) port = 80;
                if (schema.equalsIgnoreCase("https")) port = 443;
            }
        }
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

    public int getPort() {
        return port;
    }
}
