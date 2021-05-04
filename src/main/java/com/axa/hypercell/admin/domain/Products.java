package com.axa.hypercell.admin.domain;

import com.axa.hypercell.admin.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Products.
 */
@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "line_type_id")
    private Long lineTypeId;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "is_allow_multiple_sections")
    private Boolean isAllowMultipleSections;

    @OneToMany(mappedBy = "productId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "lookupTypeId", "productId" }, allowSetters = true)
    private Set<ProductsAttr> ids = new HashSet<>();

    @OneToMany(mappedBy = "productId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productId" }, allowSetters = true)
    private Set<ProductsCommissionSchema> ids = new HashSet<>();

    @OneToMany(mappedBy = "productId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productId", "clauseId" }, allowSetters = true)
    private Set<ProductClause> ids = new HashSet<>();

    @OneToMany(mappedBy = "productId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productId", "extensionId" }, allowSetters = true)
    private Set<ProductExtensions> ids = new HashSet<>();

    @OneToMany(mappedBy = "productId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ids", "ids", "productId" }, allowSetters = true)
    private Set<ProductsSection> ids = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids" }, allowSetters = true)
    private LineType lineTypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Products id(Long id) {
        this.id = id;
        return this;
    }

    public Long getLineTypeId() {
        return this.lineTypeId;
    }

    public Products lineTypeId(Long lineTypeId) {
        this.lineTypeId = lineTypeId;
        return this;
    }

    public void setLineTypeId(Long lineTypeId) {
        this.lineTypeId = lineTypeId;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public Products nameEn(String nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public Products nameAr(String nameAr) {
        this.nameAr = nameAr;
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getDescription() {
        return this.description;
    }

    public Products description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return this.status;
    }

    public Products status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getIsAllowMultipleSections() {
        return this.isAllowMultipleSections;
    }

    public Products isAllowMultipleSections(Boolean isAllowMultipleSections) {
        this.isAllowMultipleSections = isAllowMultipleSections;
        return this;
    }

    public void setIsAllowMultipleSections(Boolean isAllowMultipleSections) {
        this.isAllowMultipleSections = isAllowMultipleSections;
    }

    public Set<ProductsAttr> getIds() {
        return this.ids;
    }

    public Products ids(Set<ProductsAttr> productsAttrs) {
        this.setIds(productsAttrs);
        return this;
    }

    public Products addId(ProductsAttr productsAttr) {
        this.ids.add(productsAttr);
        productsAttr.setProductId(this);
        return this;
    }

    public Products removeId(ProductsAttr productsAttr) {
        this.ids.remove(productsAttr);
        productsAttr.setProductId(null);
        return this;
    }

    public void setIds(Set<ProductsAttr> productsAttrs) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setProductId(null));
        }
        if (productsAttrs != null) {
            productsAttrs.forEach(i -> i.setProductId(this));
        }
        this.ids = productsAttrs;
    }

    public Set<ProductsCommissionSchema> getIds() {
        return this.ids;
    }

    public Products ids(Set<ProductsCommissionSchema> productsCommissionSchemas) {
        this.setIds(productsCommissionSchemas);
        return this;
    }

    public Products addId(ProductsCommissionSchema productsCommissionSchema) {
        this.ids.add(productsCommissionSchema);
        productsCommissionSchema.setProductId(this);
        return this;
    }

    public Products removeId(ProductsCommissionSchema productsCommissionSchema) {
        this.ids.remove(productsCommissionSchema);
        productsCommissionSchema.setProductId(null);
        return this;
    }

    public void setIds(Set<ProductsCommissionSchema> productsCommissionSchemas) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setProductId(null));
        }
        if (productsCommissionSchemas != null) {
            productsCommissionSchemas.forEach(i -> i.setProductId(this));
        }
        this.ids = productsCommissionSchemas;
    }

    public Set<ProductClause> getIds() {
        return this.ids;
    }

    public Products ids(Set<ProductClause> productClauses) {
        this.setIds(productClauses);
        return this;
    }

    public Products addId(ProductClause productClause) {
        this.ids.add(productClause);
        productClause.setProductId(this);
        return this;
    }

    public Products removeId(ProductClause productClause) {
        this.ids.remove(productClause);
        productClause.setProductId(null);
        return this;
    }

    public void setIds(Set<ProductClause> productClauses) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setProductId(null));
        }
        if (productClauses != null) {
            productClauses.forEach(i -> i.setProductId(this));
        }
        this.ids = productClauses;
    }

    public Set<ProductExtensions> getIds() {
        return this.ids;
    }

    public Products ids(Set<ProductExtensions> productExtensions) {
        this.setIds(productExtensions);
        return this;
    }

    public Products addId(ProductExtensions productExtensions) {
        this.ids.add(productExtensions);
        productExtensions.setProductId(this);
        return this;
    }

    public Products removeId(ProductExtensions productExtensions) {
        this.ids.remove(productExtensions);
        productExtensions.setProductId(null);
        return this;
    }

    public void setIds(Set<ProductExtensions> productExtensions) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setProductId(null));
        }
        if (productExtensions != null) {
            productExtensions.forEach(i -> i.setProductId(this));
        }
        this.ids = productExtensions;
    }

    public Set<ProductsSection> getIds() {
        return this.ids;
    }

    public Products ids(Set<ProductsSection> productsSections) {
        this.setIds(productsSections);
        return this;
    }

    public Products addId(ProductsSection productsSection) {
        this.ids.add(productsSection);
        productsSection.setProductId(this);
        return this;
    }

    public Products removeId(ProductsSection productsSection) {
        this.ids.remove(productsSection);
        productsSection.setProductId(null);
        return this;
    }

    public void setIds(Set<ProductsSection> productsSections) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setProductId(null));
        }
        if (productsSections != null) {
            productsSections.forEach(i -> i.setProductId(this));
        }
        this.ids = productsSections;
    }

    public LineType getLineTypeId() {
        return this.lineTypeId;
    }

    public Products lineTypeId(LineType lineType) {
        this.setLineTypeId(lineType);
        return this;
    }

    public void setLineTypeId(LineType lineType) {
        this.lineTypeId = lineType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Products)) {
            return false;
        }
        return id != null && id.equals(((Products) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", lineTypeId=" + getLineTypeId() +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", isAllowMultipleSections='" + getIsAllowMultipleSections() + "'" +
            "}";
    }
}
