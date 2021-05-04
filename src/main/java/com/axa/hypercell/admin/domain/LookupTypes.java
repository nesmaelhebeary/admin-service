package com.axa.hypercell.admin.domain;

import com.axa.hypercell.admin.domain.enumeration.LookupType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LookupTypes.
 */
@Entity
@Table(name = "lookup_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LookupTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private LookupType name;

    @Enumerated(EnumType.STRING)
    @Column(name = "child_name")
    private LookupType childName;

    @OneToMany(mappedBy = "lookupTypeId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "lookupTypeId", "productId" }, allowSetters = true)
    private Set<ProductsAttr> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LookupTypes id(Long id) {
        this.id = id;
        return this;
    }

    public LookupType getName() {
        return this.name;
    }

    public LookupTypes name(LookupType name) {
        this.name = name;
        return this;
    }

    public void setName(LookupType name) {
        this.name = name;
    }

    public LookupType getChildName() {
        return this.childName;
    }

    public LookupTypes childName(LookupType childName) {
        this.childName = childName;
        return this;
    }

    public void setChildName(LookupType childName) {
        this.childName = childName;
    }

    public Set<ProductsAttr> getIds() {
        return this.ids;
    }

    public LookupTypes ids(Set<ProductsAttr> productsAttrs) {
        this.setIds(productsAttrs);
        return this;
    }

    public LookupTypes addId(ProductsAttr productsAttr) {
        this.ids.add(productsAttr);
        productsAttr.setLookupTypeId(this);
        return this;
    }

    public LookupTypes removeId(ProductsAttr productsAttr) {
        this.ids.remove(productsAttr);
        productsAttr.setLookupTypeId(null);
        return this;
    }

    public void setIds(Set<ProductsAttr> productsAttrs) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setLookupTypeId(null));
        }
        if (productsAttrs != null) {
            productsAttrs.forEach(i -> i.setLookupTypeId(this));
        }
        this.ids = productsAttrs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LookupTypes)) {
            return false;
        }
        return id != null && id.equals(((LookupTypes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LookupTypes{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", childName='" + getChildName() + "'" +
            "}";
    }
}
