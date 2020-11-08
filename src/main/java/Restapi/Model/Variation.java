package Restapi.Model;

import javax.persistence.*;

@Entity
@Table(name = "variation")
public class Variation {
    @Id
    private long id;
    private String code;
    @Column(name = "type_id", nullable = false)
    private long typeId;
    private String name;
    private String link;
    @OneToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Type type;

    public Variation() {
    }

    public Variation(long id, String code, long typeId, String name, String link) {
        this.id = id;
        this.code = code;
        this.typeId = typeId;
        this.name = name;
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeid) {
        this.typeId = typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Type getIcd10Type() {
        return type;
    }

    public void setIcd10Type(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Icd10Variation{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", typeId=" + typeId +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", icd10Type=" + type +
                '}';
    }
}
