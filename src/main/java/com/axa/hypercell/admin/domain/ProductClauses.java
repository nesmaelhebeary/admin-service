package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ProductClauses.
 */
@Entity
@Table(name = "product_clauses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductClauses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clause_id")
    private Long clauseId;

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JsonIgnoreProperties(value = "ids", allowSetters = true)
    private Products productId;

    @ManyToOne
    @JsonIgnoreProperties(value = "ids", allowSetters = true)
    private Clauses clauseId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClauseId() {
        return clauseId;
    }

    public ProductClauses clauseId(Long clauseId) {
        this.clauseId = clauseId;
        return this;
    }

    public void setClauseId(Long clauseId) {
        this.clauseId = clauseId;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductClauses productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Products getProductId() {
        return productId;
    }

    public ProductClauses productId(Products products) {
        this.productId = products;
        return this;
    }

    public void setProductId(Products products) {
        this.productId = products;
    }

    public Clauses getClauseId() {
        return clauseId;
    }

    public ProductClauses clauseId(Clauses clauses) {
        this.clauseId = clauses;
        return this;
    }

    public void setClauseId(Clauses clauses) {
        this.clauseId = clauses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductClauses)) {
            return false;
        }
        return id != null && id.equals(((ProductClauses) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductClauses{" +
            "id=" + getId() +
            ", clauseId=" + getClauseId() +
            ", productId=" + getProductId() +
            "}";
    }
}
