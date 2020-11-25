package Restapi.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "type")
public class Type {
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long id;
    @Column(nullable = false, name = "subcategory_id")
    private long subcategoryId;
    private String name;
    private String link;
    @OneToOne
    @JoinColumn(name = "subcategory_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Subcategory subcategory;

    public Type() {
    }

    public Type(long id, long subcategoryId, String name, String link) {
        this.id = id;
        this.subcategoryId = subcategoryId;
        this.name = name;
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(long subcategoryid) {
        this.subcategoryId = subcategoryid;
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

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", subcategoryId=" + subcategoryId +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", subcategory=" + subcategory +
                '}';
    }
}
