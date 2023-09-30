package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;

public class Meeting implements Serializable {

    private static final long serialVersionUID = 28741561251L;

    private String id;
    private XMLGregorianCalendar created;

}
