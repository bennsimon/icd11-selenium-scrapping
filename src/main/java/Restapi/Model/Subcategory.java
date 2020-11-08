package Restapi.Model;

import javax.persistence.*;

@Entity
@Table(name = "subcategory")
public class Subcategory {
    @Id
    private long id;
    @Column(nullable = false, name = "category_id")
    private long categoryId;
    private String name;
    private String link;
    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;

    public Subcategory() {
    }

    public Subcategory(long id, long categoryId, String name, String link) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryid) {
        this.categoryId = categoryid;
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

    public Category getIcd10Category() {
        return category;
    }

    public void setIcd10Category(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Icd10Subcategory{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
