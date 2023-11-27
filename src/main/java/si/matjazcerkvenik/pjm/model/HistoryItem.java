package si.matjazcerkvenik.pjm.model;

import si.matjazcerkvenik.pjm.util.PjmDateFormat;
import si.matjazcerkvenik.pjm.util.Utils;

public class HistoryItem {

    private long timestamp;
    private String title;
    private String href;
    private String type; // req, task, issue, comment
    private String message;

    public HistoryItem(String title, String href, String type, String message) {
        this.timestamp = System.currentTimeMillis();
        this.title = title;
        this.href = href;
        this.type = type;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormattedDate() {
        return Utils.getFormattedTimestamp(timestamp, PjmDateFormat.DATE_TIME);
    }
}
