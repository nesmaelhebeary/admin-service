package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import com.axa.hypercell.admin.domain.enumeration.AttributeName;

/**
 * A ProductsAttr.
 */
@Entity
@Table(name = "products_attr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductsAttr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "attribute_value")
    private String attributeValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "atribute_name")
    private AttributeName atributeName;

    @ManyToOne
    @JsonIgnoreProperties(value = "ids", allowSetters = true)
    private Products productId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductsAttr productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public ProductsAttr attributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
        return this;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public AttributeName getAtributeName() {
        return atributeName;
    }

    public ProductsAttr atributeName(AttributeName atributeName) {
        this.atributeName = atributeName;
        return this;
    }

    public void setAtributeName(AttributeName atributeName) {
        this.atributeName = atributeName;
    }

    public Products getProductId() {
        return productId;
    }

    public ProductsAttr productId(Products products) {
        this.productId = products;
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
        if (!(o instanceof ProductsAttr)) {
            return false;
        }
        return id != null && id.equals(((ProductsAttr) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsAttr{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", attributeValue='" + getAttributeValue() + "'" +
            ", atributeName='" + getAtributeName() + "'" +
            "}";
    }
}
