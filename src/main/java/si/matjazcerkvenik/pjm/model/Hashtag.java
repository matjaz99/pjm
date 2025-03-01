package si.matjazcerkvenik.pjm.model;

public class Hashtag {

    private String tagName;
    private String title;
    private String refUri;

    public Hashtag(String tagName, String title, String refUri) {
        this.tagName = tagName;
        this.title = title;
        this.refUri = refUri;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRefUri() {
        return refUri;
    }

    public void setRefUri(String refUri) {
        this.refUri = refUri;
    }
}
