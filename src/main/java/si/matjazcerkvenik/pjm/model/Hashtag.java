package si.matjazcerkvenik.pjm.model;

public class Hashtag {

    public String name;
    public String refUri;

    public Hashtag(String name, String refUri) {
        this.name = name;
        this.refUri = refUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefUri() {
        return refUri;
    }

    public void setRefUri(String refUri) {
        this.refUri = refUri;
    }
}
