package com.axa.hypercell.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Clauses.
 */
@Entity
@Table(name = "clauses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Clauses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "text_en")
    private String textEn;

    @Column(name = "text_ar")
    private String textAr;

    @OneToMany(mappedBy = "clauseId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductClauses> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Clauses description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextEn() {
        return textEn;
    }

    public Clauses textEn(String textEn) {
        this.textEn = textEn;
        return this;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public String getTextAr() {
        return textAr;
    }

    public Clauses textAr(String textAr) {
        this.textAr = textAr;
        return this;
    }

    public void setTextAr(String textAr) {
        this.textAr = textAr;
    }

    public Set<ProductClauses> getIds() {
        return ids;
    }

    public Clauses ids(Set<ProductClauses> productClauses) {
        this.ids = productClauses;
        return this;
    }

    public Clauses addId(ProductClauses productClauses) {
        this.ids.add(productClauses);
        productClauses.setClauseId(this);
        return this;
    }

    public Clauses removeId(ProductClauses productClauses) {
        this.ids.remove(productClauses);
        productClauses.setClauseId(null);
        return this;
    }

    public void setIds(Set<ProductClauses> productClauses) {
        this.ids = productClauses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clauses)) {
            return false;
        }
        return id != null && id.equals(((Clauses) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Clauses{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", textEn='" + getTextEn() + "'" +
            ", textAr='" + getTextAr() + "'" +
            "}";
    }
}
