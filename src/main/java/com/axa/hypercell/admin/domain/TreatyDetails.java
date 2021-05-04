package com.axa.hypercell.admin.domain;

import com.axa.hypercell.admin.domain.enumeration.ClassificationType;
import com.axa.hypercell.admin.domain.enumeration.RiskType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TreatyDetails.
 */
@Entity
@Table(name = "treaty_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TreatyDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "treaty_id")
    private Long treatyId;

    @Column(name = "maximum_limit")
    private Double maximumLimit;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "min_limit")
    private Double minLimit;

    @Column(name = "retained_amount")
    private Double retainedAmount;

    @Column(name = "ceded_amount")
    private Double cededAmount;

    @Column(name = "retained_percenatge")
    private Double retainedPercenatge;

    @Column(name = "ceded_percenatge")
    private Double cededPercenatge;

    @Column(name = "surplus")
    private Double surplus;

    @Enumerated(EnumType.STRING)
    @Column(name = "classification_type")
    private ClassificationType classificationType;

    @Column(name = "nace_code_classification")
    private String naceCodeClassification;

    @Column(name = "other_classification")
    private String otherClassification;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_type")
    private RiskType riskType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids" }, allowSetters = true)
    private Treaty treatyId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TreatyDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TreatyDetails name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTreatyId() {
        return this.treatyId;
    }

    public TreatyDetails treatyId(Long treatyId) {
        this.treatyId = treatyId;
        return this;
    }

    public void setTreatyId(Long treatyId) {
        this.treatyId = treatyId;
    }

    public Double getMaximumLimit() {
        return this.maximumLimit;
    }

    public TreatyDetails maximumLimit(Double maximumLimit) {
        this.maximumLimit = maximumLimit;
        return this;
    }

    public void setMaximumLimit(Double maximumLimit) {
        this.maximumLimit = maximumLimit;
    }

    public Long getProductId() {
        return this.productId;
    }

    public TreatyDetails productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getMinLimit() {
        return this.minLimit;
    }

    public TreatyDetails minLimit(Double minLimit) {
        this.minLimit = minLimit;
        return this;
    }

    public void setMinLimit(Double minLimit) {
        this.minLimit = minLimit;
    }

    public Double getRetainedAmount() {
        return this.retainedAmount;
    }

    public TreatyDetails retainedAmount(Double retainedAmount) {
        this.retainedAmount = retainedAmount;
        return this;
    }

    public void setRetainedAmount(Double retainedAmount) {
        this.retainedAmount = retainedAmount;
    }

    public Double getCededAmount() {
        return this.cededAmount;
    }

    public TreatyDetails cededAmount(Double cededAmount) {
        this.cededAmount = cededAmount;
        return this;
    }

    public void setCededAmount(Double cededAmount) {
        this.cededAmount = cededAmount;
    }

    public Double getRetainedPercenatge() {
        return this.retainedPercenatge;
    }

    public TreatyDetails retainedPercenatge(Double retainedPercenatge) {
        this.retainedPercenatge = retainedPercenatge;
        return this;
    }

    public void setRetainedPercenatge(Double retainedPercenatge) {
        this.retainedPercenatge = retainedPercenatge;
    }

    public Double getCededPercenatge() {
        return this.cededPercenatge;
    }

    public TreatyDetails cededPercenatge(Double cededPercenatge) {
        this.cededPercenatge = cededPercenatge;
        return this;
    }

    public void setCededPercenatge(Double cededPercenatge) {
        this.cededPercenatge = cededPercenatge;
    }

    public Double getSurplus() {
        return this.surplus;
    }

    public TreatyDetails surplus(Double surplus) {
        this.surplus = surplus;
        return this;
    }

    public void setSurplus(Double surplus) {
        this.surplus = surplus;
    }

    public ClassificationType getClassificationType() {
        return this.classificationType;
    }

    public TreatyDetails classificationType(ClassificationType classificationType) {
        this.classificationType = classificationType;
        return this;
    }

    public void setClassificationType(ClassificationType classificationType) {
        this.classificationType = classificationType;
    }

    public String getNaceCodeClassification() {
        return this.naceCodeClassification;
    }

    public TreatyDetails naceCodeClassification(String naceCodeClassification) {
        this.naceCodeClassification = naceCodeClassification;
        return this;
    }

    public void setNaceCodeClassification(String naceCodeClassification) {
        this.naceCodeClassification = naceCodeClassification;
    }

    public String getOtherClassification() {
        return this.otherClassification;
    }

    public TreatyDetails otherClassification(String otherClassification) {
        this.otherClassification = otherClassification;
        return this;
    }

    public void setOtherClassification(String otherClassification) {
        this.otherClassification = otherClassification;
    }

    public RiskType getRiskType() {
        return this.riskType;
    }

    public TreatyDetails riskType(RiskType riskType) {
        this.riskType = riskType;
        return this;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }

    public Treaty getTreatyId() {
        return this.treatyId;
    }

    public TreatyDetails treatyId(Treaty treaty) {
        this.setTreatyId(treaty);
        return this;
    }

    public void setTreatyId(Treaty treaty) {
        this.treatyId = treaty;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TreatyDetails)) {
            return false;
        }
        return id != null && id.equals(((TreatyDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TreatyDetails{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", treatyId=" + getTreatyId() +
            ", maximumLimit=" + getMaximumLimit() +
            ", productId=" + getProductId() +
            ", minLimit=" + getMinLimit() +
            ", retainedAmount=" + getRetainedAmount() +
            ", cededAmount=" + getCededAmount() +
            ", retainedPercenatge=" + getRetainedPercenatge() +
            ", cededPercenatge=" + getCededPercenatge() +
            ", surplus=" + getSurplus() +
            ", classificationType='" + getClassificationType() + "'" +
            ", naceCodeClassification='" + getNaceCodeClassification() + "'" +
            ", otherClassification='" + getOtherClassification() + "'" +
            ", riskType='" + getRiskType() + "'" +
            "}";
    }
}
