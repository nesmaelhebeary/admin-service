package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductsSectionIncludeList.
 */
@Entity
@Table(name = "products_section_include_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductsSectionIncludeList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_id")
    private Long sectionId;

    @Column(name = "other_section_id")
    private Long otherSectionId;

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

    public ProductsSectionIncludeList id(Long id) {
        this.id = id;
        return this;
    }

    public Long getSectionId() {
        return this.sectionId;
    }

    public ProductsSectionIncludeList sectionId(Long sectionId) {
        this.sectionId = sectionId;
        return this;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getOtherSectionId() {
        return this.otherSectionId;
    }

    public ProductsSectionIncludeList otherSectionId(Long otherSectionId) {
        this.otherSectionId = otherSectionId;
        return this;
    }

    public void setOtherSectionId(Long otherSectionId) {
        this.otherSectionId = otherSectionId;
    }

    public ProductsSection getSectionId() {
        return this.sectionId;
    }

    public ProductsSectionIncludeList sectionId(ProductsSection productsSection) {
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
        if (!(o instanceof ProductsSectionIncludeList)) {
            return false;
        }
        return id != null && id.equals(((ProductsSectionIncludeList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsSectionIncludeList{" +
            "id=" + getId() +
            ", sectionId=" + getSectionId() +
            ", otherSectionId=" + getOtherSectionId() +
            "}";
    }
}
