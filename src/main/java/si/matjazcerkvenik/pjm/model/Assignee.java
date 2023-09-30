package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;

import java.io.Serializable;

public class Assignee implements Serializable {

    private static final long serialVersionUID = 34157915024L;

    private String memberRefId;

    public String getMemberRefId() {
        return memberRefId;
    }

    @XmlAttribute(name = "memberRefId")
    public void setMemberRefId(String memberRefId) {
        this.memberRefId = memberRefId;
    }
}
