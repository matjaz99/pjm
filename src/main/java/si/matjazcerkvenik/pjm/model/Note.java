package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Note implements Serializable {

    private static final long serialVersionUID = 33215484661L;

    private String id;
    private String title;
    private String description;
    private XMLGregorianCalendar created;

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

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public XMLGregorianCalendar getCreated() {
        return created;
    }

    @XmlAttribute
    public void setCreated(XMLGregorianCalendar created) {
        this.created = created;
    }

    /**
     * Return abstract representation of this object.
     * @return ai
     */
    public AbstractItem toAbstractItem() {
        AbstractItem ai = new AbstractItem();
        ai.setTitle(this.title);
        ai.setItemType("note");
        ai.setText(this.description);
        ai.setUrl("/project/notes");
        return ai;
    }

    public List<Hashtag> getHashtags() {
        List<Hashtag> htList = new ArrayList<>();
        List<String> list = Utils.findHashtags(description);
        for (String s : list) {
            Hashtag ht = new Hashtag(s, title, "/project/notes");
            htList.add(ht);
        }
        return htList;
    }
}
