package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Deductibles.
 */
@Entity
@Table(name = "deductibles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deductibles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_id")
    private Long sectionId;

    @Column(name = "text_ar")
    private String textAr;

    @Column(name = "text_en")
    private String textEn;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids", "ids", "productId" }, allowSetters = true)
    private ProductsSection sectionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Deductibles id(Long id) {
        this.id = id;
        return this;
    }

    public Long getSectionId() {
        return this.sectionId;
    }

    public Deductibles sectionId(Long sectionId) {
        this.sectionId = sectionId;
        return this;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getTextAr() {
        return this.textAr;
    }

    public Deductibles textAr(String textAr) {
        this.textAr = textAr;
        return this;
    }

    public void setTextAr(String textAr) {
        this.textAr = textAr;
    }

    public String getTextEn() {
        return this.textEn;
    }

    public Deductibles textEn(String textEn) {
        this.textEn = textEn;
        return this;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public ProductsSection getSectionId() {
        return this.sectionId;
    }

    public Deductibles sectionId(ProductsSection productsSection) {
        this.setSectionId(productsSection);
        return this;
    }

    public void setSectionId(ProductsSection productsSection) {
        this.sectionId = productsSection;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deductibles)) {
            return false;
        }
        return id != null && id.equals(((Deductibles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deductibles{" +
            "id=" + getId() +
            ", sectionId=" + getSectionId() +
            ", textAr='" + getTextAr() + "'" +
            ", textEn='" + getTextEn() + "'" +
            "}";
    }
}
