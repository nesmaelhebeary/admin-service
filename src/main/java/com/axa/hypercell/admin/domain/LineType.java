package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LineType.
 */
@Entity
@Table(name = "line_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LineType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "lineTypeId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ids", "ids", "ids", "ids", "ids", "lineTypeId" }, allowSetters = true)
    private Set<Products> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LineType id(Long id) {
        this.id = id;
        return this;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public LineType nameEn(String nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public LineType nameAr(String nameAr) {
        this.nameAr = nameAr;
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getDescription() {
        return this.description;
    }

    public LineType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Products> getIds() {
        return this.ids;
    }

    public LineType ids(Set<Products> products) {
        this.setIds(products);
        return this;
    }

    public LineType addId(Products products) {
        this.ids.add(products);
        products.setLineTypeId(this);
        return this;
    }

    public LineType removeId(Products products) {
        this.ids.remove(products);
        products.setLineTypeId(null);
        return this;
    }

    public void setIds(Set<Products> products) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setLineTypeId(null));
        }
        if (products != null) {
            products.forEach(i -> i.setLineTypeId(this));
        }
        this.ids = products;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineType)) {
            return false;
        }
        return id != null && id.equals(((LineType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LineType{" +
            "id=" + getId() +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
