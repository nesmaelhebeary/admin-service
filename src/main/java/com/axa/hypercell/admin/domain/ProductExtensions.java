package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductExtensions.
 */
@Entity
@Table(name = "product_extensions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductExtensions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "extension_id")
    private Long extensionId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids", "ids", "ids", "ids", "ids", "lineTypeId" }, allowSetters = true)
    private Products productId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids", "ids" }, allowSetters = true)
    private Extensions extensionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductExtensions id(Long id) {
        this.id = id;
        return this;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductExtensions productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getExtensionId() {
        return this.extensionId;
    }

    public ProductExtensions extensionId(Long extensionId) {
        this.extensionId = extensionId;
        return this;
    }

    public void setExtensionId(Long extensionId) {
        this.extensionId = extensionId;
    }

    public Products getProductId() {
        return this.productId;
    }

    public ProductExtensions productId(Products products) {
        this.setProductId(products);
        return this;
    }

    public void setProductId(Products products) {
        this.productId = products;
    }

    public Extensions getExtensionId() {
        return this.extensionId;
    }

    public ProductExtensions extensionId(Extensions extensions) {
        this.setExtensionId(extensions);
        return this;
    }

    public void setExtensionId(Extensions extensions) {
        this.extensionId = extensions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductExtensions)) {
            return false;
        }
        return id != null && id.equals(((ProductExtensions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductExtensions{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", extensionId=" + getExtensionId() +
            "}";
    }
}
