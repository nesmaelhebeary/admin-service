package com.axa.hypercell.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TermsAndConditions.
 */
@Entity
@Table(name = "terms_and_conditions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TermsAndConditions implements Serializable {

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

    @OneToMany(mappedBy = "tAndCId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductTAndC> ids = new HashSet<>();

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

    public TermsAndConditions description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextEn() {
        return textEn;
    }

    public TermsAndConditions textEn(String textEn) {
        this.textEn = textEn;
        return this;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public String getTextAr() {
        return textAr;
    }

    public TermsAndConditions textAr(String textAr) {
        this.textAr = textAr;
        return this;
    }

    public void setTextAr(String textAr) {
        this.textAr = textAr;
    }

    public Set<ProductTAndC> getIds() {
        return ids;
    }

    public TermsAndConditions ids(Set<ProductTAndC> productTAndCS) {
        this.ids = productTAndCS;
        return this;
    }

    public TermsAndConditions addId(ProductTAndC productTAndC) {
        this.ids.add(productTAndC);
        productTAndC.setTAndCId(this);
        return this;
    }

    public TermsAndConditions removeId(ProductTAndC productTAndC) {
        this.ids.remove(productTAndC);
        productTAndC.setTAndCId(null);
        return this;
    }

    public void setIds(Set<ProductTAndC> productTAndCS) {
        this.ids = productTAndCS;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TermsAndConditions)) {
            return false;
        }
        return id != null && id.equals(((TermsAndConditions) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TermsAndConditions{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", textEn='" + getTextEn() + "'" +
            ", textAr='" + getTextAr() + "'" +
            "}";
    }
}
