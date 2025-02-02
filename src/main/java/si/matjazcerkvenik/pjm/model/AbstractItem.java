package si.matjazcerkvenik.pjm.model;

public class AbstractItem {

    private String itemType;
    private String title;
    private String url;
    private String text;


    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItem() {
        return "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if (text != null && text.length() > 280) {
            this.text = text.substring(0, 280) + "...";
        }
    }
}
