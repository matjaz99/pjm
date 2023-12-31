package si.matjazcerkvenik.pjm.model;

import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Members implements Serializable {

    private static final long serialVersionUID = 29929481561L;

    private List<Member> list = new ArrayList<>();

    public List<Member> getList() {
        return list;
    }

    @XmlElement(name = "member")
    public void setList(List<Member> list) {
        this.list = list;
    }

    public void addNewMember(Member member) {
        if (list == null) list = new ArrayList<>();
        list.add(member);
    }

    /**
     * Find member by his name and lastName (separated by whitespace
     * @param id
     * @return member
     */
    public Member findMember(String id) {
        for (Member m : list) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }
}
