package com.axa.hypercell.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import com.axa.hypercell.admin.domain.enumeration.Status;

/**
 * A RIBrokers.
 */
@Entity
@Table(name = "ri_brokers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RIBrokers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "registeration_number")
    private String registerationNumber;

    @Column(name = "commission_percentage")
    private Double commissionPercentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "contact_dial")
    private String contactDial;

    @Column(name = "contact_email")
    private String contactEmail;

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

    public RIBrokers name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterationNumber() {
        return registerationNumber;
    }

    public RIBrokers registerationNumber(String registerationNumber) {
        this.registerationNumber = registerationNumber;
        return this;
    }

    public void setRegisterationNumber(String registerationNumber) {
        this.registerationNumber = registerationNumber;
    }

    public Double getCommissionPercentage() {
        return commissionPercentage;
    }

    public RIBrokers commissionPercentage(Double commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
        return this;
    }

    public void setCommissionPercentage(Double commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public Status getStatus() {
        return status;
    }

    public RIBrokers status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getContactDial() {
        return contactDial;
    }

    public RIBrokers contactDial(String contactDial) {
        this.contactDial = contactDial;
        return this;
    }

    public void setContactDial(String contactDial) {
        this.contactDial = contactDial;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public RIBrokers contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RIBrokers)) {
            return false;
        }
        return id != null && id.equals(((RIBrokers) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RIBrokers{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", registerationNumber='" + getRegisterationNumber() + "'" +
            ", commissionPercentage=" + getCommissionPercentage() +
            ", status='" + getStatus() + "'" +
            ", contactDial='" + getContactDial() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            "}";
    }
}
