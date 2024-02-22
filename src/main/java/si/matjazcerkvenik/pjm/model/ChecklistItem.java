package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

public class ChecklistItem implements Serializable {

    private static final long serialVersionUID = 2412541255L;

    private String id;
    private String name;
    private XMLGregorianCalendar checkedDate;
    private boolean checked;
    private String comment;

    public ChecklistItem() {
    }

    public ChecklistItem(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public XMLGregorianCalendar getCheckedDate() {
        return checkedDate;
    }

    @XmlAttribute(name = "checkedDate")
    public void setCheckedDate(XMLGregorianCalendar checkedDate) {
        this.checkedDate = checkedDate;
    }

    public boolean isChecked() {
        return checked;
    }

    @XmlAttribute(name = "checked")
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getComment() {
        return comment;
    }

    @XmlValue
    public void setComment(String comment) {
        this.comment = comment;
    }


}
