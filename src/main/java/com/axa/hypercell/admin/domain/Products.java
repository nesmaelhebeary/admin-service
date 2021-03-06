package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "name")
    private String name;

    @Column(name = "line_type_id")
    private Long lineTypeId;

    @OneToMany(mappedBy = "productId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductsAttr> ids = new HashSet<>();

    @OneToMany(mappedBy = "productId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductClauses> ids = new HashSet<>();

    @OneToMany(mappedBy = "productId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductTAndC> ids = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "ids", allowSetters = true)
    private LineType lineTypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Products name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLineTypeId() {
        return lineTypeId;
    }

    public Products lineTypeId(Long lineTypeId) {
        this.lineTypeId = lineTypeId;
        return this;
    }

    public void setLineTypeId(Long lineTypeId) {
        this.lineTypeId = lineTypeId;
    }

    public Set<ProductsAttr> getIds() {
        return ids;
    }

    public Products ids(Set<ProductsAttr> productsAttrs) {
        this.ids = productsAttrs;
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
        this.ids = productsAttrs;
    }

    public Set<ProductClauses> getIds() {
        return ids;
    }

    public Products ids(Set<ProductClauses> productClauses) {
        this.ids = productClauses;
        return this;
    }

    public Products addId(ProductClauses productClauses) {
        this.ids.add(productClauses);
        productClauses.setProductId(this);
        return this;
    }

    public Products removeId(ProductClauses productClauses) {
        this.ids.remove(productClauses);
        productClauses.setProductId(null);
        return this;
    }

    public void setIds(Set<ProductClauses> productClauses) {
        this.ids = productClauses;
    }

    public Set<ProductTAndC> getIds() {
        return ids;
    }

    public Products ids(Set<ProductTAndC> productTAndCS) {
        this.ids = productTAndCS;
        return this;
    }

    public Products addId(ProductTAndC productTAndC) {
        this.ids.add(productTAndC);
        productTAndC.setProductId(this);
        return this;
    }

    public Products removeId(ProductTAndC productTAndC) {
        this.ids.remove(productTAndC);
        productTAndC.setProductId(null);
        return this;
    }

    public void setIds(Set<ProductTAndC> productTAndCS) {
        this.ids = productTAndCS;
    }

    public LineType getLineTypeId() {
        return lineTypeId;
    }

    public Products lineTypeId(LineType lineType) {
        this.lineTypeId = lineType;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lineTypeId=" + getLineTypeId() +
            "}";
    }
}
