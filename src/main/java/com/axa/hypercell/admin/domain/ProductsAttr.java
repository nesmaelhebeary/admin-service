package com.axa.hypercell.admin.domain;

import com.axa.hypercell.admin.domain.enumeration.AttrType;
import com.axa.hypercell.admin.domain.enumeration.DataType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @Column(name = "atribute_name")
    private String atributeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private DataType dataType;

    @Column(name = "is_mandatory_for_quotation")
    private Boolean isMandatoryForQuotation;

    @Column(name = "is_mandatory_for_policy")
    private Boolean isMandatoryForPolicy;

    @Enumerated(EnumType.STRING)
    @Column(name = "attr_type")
    private AttrType attrType;

    @Column(name = "lookup_type_id")
    private Long lookupTypeId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids" }, allowSetters = true)
    private LookupTypes lookupTypeId;

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

    public ProductsAttr id(Long id) {
        this.id = id;
        return this;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductsAttr productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getAttributeValue() {
        return this.attributeValue;
    }

    public ProductsAttr attributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
        return this;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAtributeName() {
        return this.atributeName;
    }

    public ProductsAttr atributeName(String atributeName) {
        this.atributeName = atributeName;
        return this;
    }

    public void setAtributeName(String atributeName) {
        this.atributeName = atributeName;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public ProductsAttr dataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Boolean getIsMandatoryForQuotation() {
        return this.isMandatoryForQuotation;
    }

    public ProductsAttr isMandatoryForQuotation(Boolean isMandatoryForQuotation) {
        this.isMandatoryForQuotation = isMandatoryForQuotation;
        return this;
    }

    public void setIsMandatoryForQuotation(Boolean isMandatoryForQuotation) {
        this.isMandatoryForQuotation = isMandatoryForQuotation;
    }

    public Boolean getIsMandatoryForPolicy() {
        return this.isMandatoryForPolicy;
    }

    public ProductsAttr isMandatoryForPolicy(Boolean isMandatoryForPolicy) {
        this.isMandatoryForPolicy = isMandatoryForPolicy;
        return this;
    }

    public void setIsMandatoryForPolicy(Boolean isMandatoryForPolicy) {
        this.isMandatoryForPolicy = isMandatoryForPolicy;
    }

    public AttrType getAttrType() {
        return this.attrType;
    }

    public ProductsAttr attrType(AttrType attrType) {
        this.attrType = attrType;
        return this;
    }

    public void setAttrType(AttrType attrType) {
        this.attrType = attrType;
    }

    public Long getLookupTypeId() {
        return this.lookupTypeId;
    }

    public ProductsAttr lookupTypeId(Long lookupTypeId) {
        this.lookupTypeId = lookupTypeId;
        return this;
    }

    public void setLookupTypeId(Long lookupTypeId) {
        this.lookupTypeId = lookupTypeId;
    }

    public LookupTypes getLookupTypeId() {
        return this.lookupTypeId;
    }

    public ProductsAttr lookupTypeId(LookupTypes lookupTypes) {
        this.setLookupTypeId(lookupTypes);
        return this;
    }

    public void setLookupTypeId(LookupTypes lookupTypes) {
        this.lookupTypeId = lookupTypes;
    }

    public Products getProductId() {
        return this.productId;
    }

    public ProductsAttr productId(Products products) {
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
        if (!(o instanceof ProductsAttr)) {
            return false;
        }
        return id != null && id.equals(((ProductsAttr) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsAttr{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", attributeValue='" + getAttributeValue() + "'" +
            ", atributeName='" + getAtributeName() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", isMandatoryForQuotation='" + getIsMandatoryForQuotation() + "'" +
            ", isMandatoryForPolicy='" + getIsMandatoryForPolicy() + "'" +
            ", attrType='" + getAttrType() + "'" +
            ", lookupTypeId=" + getLookupTypeId() +
            "}";
    }
}
