package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Assignee {

    private String memberRefId;

    public String getMemberRefId() {
        return memberRefId;
    }

    @XmlAttribute(name = "memberRefId")
    public void setMemberRefId(String memberRefId) {
        this.memberRefId = memberRefId;
    }
}
