package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductsCommissionSchema.
 */
@Entity
@Table(name = "products_commission_schema")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductsCommissionSchema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "display_in_template")
    private Boolean displayInTemplate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids", "ids", "ids", "ids", "ids", "lineTypeId" }, allowSetters = true)
    private Products productId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductsCommissionSchema id(Long id) {
        this.id = id;
        return this;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductsCommissionSchema productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public ProductsCommissionSchema nameEn(String nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public ProductsCommissionSchema nameAr(String nameAr) {
        this.nameAr = nameAr;
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public Boolean getDisplayInTemplate() {
        return this.displayInTemplate;
    }

    public ProductsCommissionSchema displayInTemplate(Boolean displayInTemplate) {
        this.displayInTemplate = displayInTemplate;
        return this;
    }

    public void setDisplayInTemplate(Boolean displayInTemplate) {
        this.displayInTemplate = displayInTemplate;
    }

    public Products getProductId() {
        return this.productId;
    }

    public ProductsCommissionSchema productId(Products products) {
        this.setProductId(products);
        return this;
    }

    public void setProductId(Products products) {
        this.productId = products;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductsCommissionSchema)) {
            return false;
        }
        return id != null && id.equals(((ProductsCommissionSchema) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsCommissionSchema{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", displayInTemplate='" + getDisplayInTemplate() + "'" +
            "}";
    }
}
