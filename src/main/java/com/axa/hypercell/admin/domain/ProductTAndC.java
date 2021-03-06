package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ProductTAndC.
 */
@Entity
@Table(name = "product_t_and_c")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductTAndC implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "t_and_c_id")
    private Long tAndCId;

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JsonIgnoreProperties(value = "ids", allowSetters = true)
    private Products productId;

    @ManyToOne
    @JsonIgnoreProperties(value = "ids", allowSetters = true)
    private TermsAndConditions tAndCId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long gettAndCId() {
        return tAndCId;
    }

    public ProductTAndC tAndCId(Long tAndCId) {
        this.tAndCId = tAndCId;
        return this;
    }

    public void settAndCId(Long tAndCId) {
        this.tAndCId = tAndCId;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductTAndC productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Products getProductId() {
        return productId;
    }

    public ProductTAndC productId(Products products) {
        this.productId = products;
        return this;
    }

    public void setProductId(Products products) {
        this.productId = products;
    }

    public TermsAndConditions getTAndCId() {
        return tAndCId;
    }

    public ProductTAndC tAndCId(TermsAndConditions termsAndConditions) {
        this.tAndCId = termsAndConditions;
        return this;
    }

    public void setTAndCId(TermsAndConditions termsAndConditions) {
        this.tAndCId = termsAndConditions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductTAndC)) {
            return false;
        }
        return id != null && id.equals(((ProductTAndC) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductTAndC{" +
            "id=" + getId() +
            ", tAndCId=" + gettAndCId() +
            ", productId=" + getProductId() +
            "}";
    }
}
