package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import si.matjazcerkvenik.pjm.util.PjmDateFormat;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meeting implements Serializable {

    private static final long serialVersionUID = 28741561251L;

    private String id;
    private String title;
    private String description;
    private XMLGregorianCalendar plannedDate;
    // concluded actually means archived (not editable anymore)
    private boolean concluded = false;
    private int rating;

    public String getId() {
        return id;
    }

    @XmlElement
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

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public XMLGregorianCalendar getPlannedDate() {
        return plannedDate;
    }

    @XmlAttribute
    public void setPlannedDate(XMLGregorianCalendar plannedDate) {
        this.plannedDate = plannedDate;
    }

    public boolean isConcluded() {
        return concluded;
    }

    @XmlAttribute
    public void setConcluded(boolean concluded) {
        this.concluded = concluded;
    }

    public int getRating() {
        return rating;
    }

    @XmlAttribute
    public void setRating(int rating) {
        this.rating = rating;
    }

    // other methods

    public String getPlannedDateAndOnlyTheDate() {
        return Utils.getFormattedTimestamp(plannedDate, PjmDateFormat.DATE_SI);
    }

    public boolean isPastTheDate() {
        if (plannedDate == null) return false;
        if ((System.currentTimeMillis() + 12 * 3600 * 1000) >= Utils.getMillis(plannedDate)) return true;
        return false;
    }

    /**
     * Return abstract representation of this object.
     * @return ai
     */
    public AbstractItem toAbstractItem() {
        AbstractItem ai = new AbstractItem();
        ai.setTitle(this.title);
        ai.setItemType("meeting");
        ai.setText(this.description);
        ai.setUrl("/project/meeting?meetingId=" + this.id);
        return ai;
    }

    public List<Hashtag> getHashtags() {
        List<Hashtag> htList = new ArrayList<>();
        List<String> list = Utils.findHashtags(description);
        for (String s : list) {
            Hashtag ht = new Hashtag(s, "/project/meeting?meetingId=" + this.id);
            htList.add(ht);
        }
        System.out.println("Meeting: ht-size: " + htList.size() + "; ==" + htList.toString());
        return htList;
    }
}
