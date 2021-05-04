package com.axa.hypercell.admin.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FireLineSettings.
 */
@Entity
@Table(name = "fire_line_settings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FireLineSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "from_value")
    private Long fromValue;

    @Column(name = "to_value")
    private Long toValue;

    @Column(name = "currency")
    private String currency;

    @Column(name = "coverage_percentage")
    private Long coveragePercentage;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FireLineSettings id(Long id) {
        this.id = id;
        return this;
    }

    public String getClassName() {
        return this.className;
    }

    public FireLineSettings className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getFromValue() {
        return this.fromValue;
    }

    public FireLineSettings fromValue(Long fromValue) {
        this.fromValue = fromValue;
        return this;
    }

    public void setFromValue(Long fromValue) {
        this.fromValue = fromValue;
    }

    public Long getToValue() {
        return this.toValue;
    }

    public FireLineSettings toValue(Long toValue) {
        this.toValue = toValue;
        return this;
    }

    public void setToValue(Long toValue) {
        this.toValue = toValue;
    }

    public String getCurrency() {
        return this.currency;
    }

    public FireLineSettings currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getCoveragePercentage() {
        return this.coveragePercentage;
    }

    public FireLineSettings coveragePercentage(Long coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
        return this;
    }

    public void setCoveragePercentage(Long coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FireLineSettings)) {
            return false;
        }
        return id != null && id.equals(((FireLineSettings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FireLineSettings{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", fromValue=" + getFromValue() +
            ", toValue=" + getToValue() +
            ", currency='" + getCurrency() + "'" +
            ", coveragePercentage=" + getCoveragePercentage() +
            "}";
    }
}
