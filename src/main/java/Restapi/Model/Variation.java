package Restapi.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "variation")
public class Variation {
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Variation{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", typeId=" + typeId +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                '}';
    }
}
