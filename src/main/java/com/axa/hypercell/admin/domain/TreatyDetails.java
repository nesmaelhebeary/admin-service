package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

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

    @Column(name = "min_limit")
    private Double minLimit;

    @Column(name = "retained")
    private Double retained;

    @Column(name = "quota_shared")
    private Double quotaShared;

    @Column(name = "surplus")
    private Double surplus;

    @Column(name = "auto_fac")
    private Double autoFac;

    @ManyToOne
    @JsonIgnoreProperties(value = "ids", allowSetters = true)
    private Treaty treatyId;

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

    public TreatyDetails name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTreatyId() {
        return treatyId;
    }

    public TreatyDetails treatyId(Long treatyId) {
        this.treatyId = treatyId;
        return this;
    }

    public void setTreatyId(Long treatyId) {
        this.treatyId = treatyId;
    }

    public Double getMaximumLimit() {
        return maximumLimit;
    }

    public TreatyDetails maximumLimit(Double maximumLimit) {
        this.maximumLimit = maximumLimit;
        return this;
    }

    public void setMaximumLimit(Double maximumLimit) {
        this.maximumLimit = maximumLimit;
    }

    public Double getMinLimit() {
        return minLimit;
    }

    public TreatyDetails minLimit(Double minLimit) {
        this.minLimit = minLimit;
        return this;
    }

    public void setMinLimit(Double minLimit) {
        this.minLimit = minLimit;
    }

    public Double getRetained() {
        return retained;
    }

    public TreatyDetails retained(Double retained) {
        this.retained = retained;
        return this;
    }

    public void setRetained(Double retained) {
        this.retained = retained;
    }

    public Double getQuotaShared() {
        return quotaShared;
    }

    public TreatyDetails quotaShared(Double quotaShared) {
        this.quotaShared = quotaShared;
        return this;
    }

    public void setQuotaShared(Double quotaShared) {
        this.quotaShared = quotaShared;
    }

    public Double getSurplus() {
        return surplus;
    }

    public TreatyDetails surplus(Double surplus) {
        this.surplus = surplus;
        return this;
    }

    public void setSurplus(Double surplus) {
        this.surplus = surplus;
    }

    public Double getAutoFac() {
        return autoFac;
    }

    public TreatyDetails autoFac(Double autoFac) {
        this.autoFac = autoFac;
        return this;
    }

    public void setAutoFac(Double autoFac) {
        this.autoFac = autoFac;
    }

    public Treaty getTreatyId() {
        return treatyId;
    }

    public TreatyDetails treatyId(Treaty treaty) {
        this.treatyId = treaty;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TreatyDetails{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", treatyId=" + getTreatyId() +
            ", maximumLimit=" + getMaximumLimit() +
            ", minLimit=" + getMinLimit() +
            ", retained=" + getRetained() +
            ", quotaShared=" + getQuotaShared() +
            ", surplus=" + getSurplus() +
            ", autoFac=" + getAutoFac() +
            "}";
    }
}
