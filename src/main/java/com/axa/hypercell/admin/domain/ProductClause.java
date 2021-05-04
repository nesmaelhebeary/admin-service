package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductClause.
 */
@Entity
@Table(name = "product_clause")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductClause implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "clause_id")
    private Long clauseId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids", "ids", "ids", "ids", "ids", "lineTypeId" }, allowSetters = true)
    private Products productId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids" }, allowSetters = true)
    private SystemClauses clauseId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductClause id(Long id) {
        this.id = id;
        return this;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductClause productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getClauseId() {
        return this.clauseId;
    }

    public ProductClause clauseId(Long clauseId) {
        this.clauseId = clauseId;
        return this;
    }

    public void setClauseId(Long clauseId) {
        this.clauseId = clauseId;
    }

    public Products getProductId() {
        return this.productId;
    }

    public ProductClause productId(Products products) {
        this.setProductId(products);
        return this;
    }

    public void setProductId(Products products) {
        this.productId = products;
    }

    public SystemClauses getClauseId() {
        return this.clauseId;
    }

    public ProductClause clauseId(SystemClauses systemClauses) {
        this.setClauseId(systemClauses);
        return this;
    }

    public void setClauseId(SystemClauses systemClauses) {
        this.clauseId = systemClauses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductClause)) {
            return false;
        }
        return id != null && id.equals(((ProductClause) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductClause{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", clauseId=" + getClauseId() +
            "}";
    }
}
