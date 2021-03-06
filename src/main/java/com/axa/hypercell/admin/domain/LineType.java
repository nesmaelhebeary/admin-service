package com.axa.hypercell.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "lineTypeId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Products> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LineType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Products> getIds() {
        return ids;
    }

    public LineType ids(Set<Products> products) {
        this.ids = products;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LineType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
