package com.axa.hypercell.admin.domain;

import com.axa.hypercell.admin.domain.enumeration.DefaultSumUp;
import com.axa.hypercell.admin.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductsSection.
 */
@Entity
@Table(name = "products_section")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductsSection implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_sum_up")
    private DefaultSumUp defaultSumUp;

    @OneToMany(mappedBy = "sectionId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sectionId" }, allowSetters = true)
    private Set<ProductsSectionIncludeList> ids = new HashSet<>();

    @OneToMany(mappedBy = "sectionId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sectionId" }, allowSetters = true)
    private Set<Deductibles> ids = new HashSet<>();

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

    public ProductsSection id(Long id) {
        this.id = id;
        return this;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductsSection productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public ProductsSection nameEn(String nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public ProductsSection nameAr(String nameAr) {
        this.nameAr = nameAr;
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public Status getStatus() {
        return this.status;
    }

    public ProductsSection status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DefaultSumUp getDefaultSumUp() {
        return this.defaultSumUp;
    }

    public ProductsSection defaultSumUp(DefaultSumUp defaultSumUp) {
        this.defaultSumUp = defaultSumUp;
        return this;
    }

    public void setDefaultSumUp(DefaultSumUp defaultSumUp) {
        this.defaultSumUp = defaultSumUp;
    }

    public Set<ProductsSectionIncludeList> getIds() {
        return this.ids;
    }

    public ProductsSection ids(Set<ProductsSectionIncludeList> productsSectionIncludeLists) {
        this.setIds(productsSectionIncludeLists);
        return this;
    }

    public ProductsSection addId(ProductsSectionIncludeList productsSectionIncludeList) {
        this.ids.add(productsSectionIncludeList);
        productsSectionIncludeList.setSectionId(this);
        return this;
    }

    public ProductsSection removeId(ProductsSectionIncludeList productsSectionIncludeList) {
        this.ids.remove(productsSectionIncludeList);
        productsSectionIncludeList.setSectionId(null);
        return this;
    }

    public void setIds(Set<ProductsSectionIncludeList> productsSectionIncludeLists) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setSectionId(null));
        }
        if (productsSectionIncludeLists != null) {
            productsSectionIncludeLists.forEach(i -> i.setSectionId(this));
        }
        this.ids = productsSectionIncludeLists;
    }

    public Set<Deductibles> getIds() {
        return this.ids;
    }

    public ProductsSection ids(Set<Deductibles> deductibles) {
        this.setIds(deductibles);
        return this;
    }

    public ProductsSection addId(Deductibles deductibles) {
        this.ids.add(deductibles);
        deductibles.setSectionId(this);
        return this;
    }

    public ProductsSection removeId(Deductibles deductibles) {
        this.ids.remove(deductibles);
        deductibles.setSectionId(null);
        return this;
    }

    public void setIds(Set<Deductibles> deductibles) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setSectionId(null));
        }
        if (deductibles != null) {
            deductibles.forEach(i -> i.setSectionId(this));
        }
        this.ids = deductibles;
    }

    public Products getProductId() {
        return this.productId;
    }

    public ProductsSection productId(Products products) {
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
        if (!(o instanceof ProductsSection)) {
            return false;
        }
        return id != null && id.equals(((ProductsSection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsSection{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", status='" + getStatus() + "'" +
            ", defaultSumUp='" + getDefaultSumUp() + "'" +
            "}";
    }
}
