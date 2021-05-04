package com.axa.hypercell.admin.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NaceCode.
 */
@Entity
@Table(name = "nace_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NaceCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity")
    private String activity;

    @Column(name = "code")
    private String code;

    @Column(name = "global_class")
    private String globalClass;

    @Column(name = "auto_fac_capacity")
    private Integer autoFacCapacity;

    @Column(name = "local_class")
    private String localClass;

    @Column(name = "bo_rate")
    private Float boRate;

    @Column(name = "pd_rate")
    private Float pdRate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NaceCode id(Long id) {
        this.id = id;
        return this;
    }

    public String getActivity() {
        return this.activity;
    }

    public NaceCode activity(String activity) {
        this.activity = activity;
        return this;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getCode() {
        return this.code;
    }

    public NaceCode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGlobalClass() {
        return this.globalClass;
    }

    public NaceCode globalClass(String globalClass) {
        this.globalClass = globalClass;
        return this;
    }

    public void setGlobalClass(String globalClass) {
        this.globalClass = globalClass;
    }

    public Integer getAutoFacCapacity() {
        return this.autoFacCapacity;
    }

    public NaceCode autoFacCapacity(Integer autoFacCapacity) {
        this.autoFacCapacity = autoFacCapacity;
        return this;
    }

    public void setAutoFacCapacity(Integer autoFacCapacity) {
        this.autoFacCapacity = autoFacCapacity;
    }

    public String getLocalClass() {
        return this.localClass;
    }

    public NaceCode localClass(String localClass) {
        this.localClass = localClass;
        return this;
    }

    public void setLocalClass(String localClass) {
        this.localClass = localClass;
    }

    public Float getBoRate() {
        return this.boRate;
    }

    public NaceCode boRate(Float boRate) {
        this.boRate = boRate;
        return this;
    }

    public void setBoRate(Float boRate) {
        this.boRate = boRate;
    }

    public Float getPdRate() {
        return this.pdRate;
    }

    public NaceCode pdRate(Float pdRate) {
        this.pdRate = pdRate;
        return this;
    }

    public void setPdRate(Float pdRate) {
        this.pdRate = pdRate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NaceCode)) {
            return false;
        }
        return id != null && id.equals(((NaceCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NaceCode{" +
            "id=" + getId() +
            ", activity='" + getActivity() + "'" +
            ", code='" + getCode() + "'" +
            ", globalClass='" + getGlobalClass() + "'" +
            ", autoFacCapacity=" + getAutoFacCapacity() +
            ", localClass='" + getLocalClass() + "'" +
            ", boRate=" + getBoRate() +
            ", pdRate=" + getPdRate() +
            "}";
    }
}
