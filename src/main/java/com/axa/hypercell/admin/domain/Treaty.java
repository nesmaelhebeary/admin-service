package com.axa.hypercell.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Treaty.
 */
@Entity
@Table(name = "treaty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Treaty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "line_type_id")
    private Long lineTypeId;

    @OneToMany(mappedBy = "treatyId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TreatyDetails> ids = new HashSet<>();

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

    public Treaty name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLineTypeId() {
        return lineTypeId;
    }

    public Treaty lineTypeId(Long lineTypeId) {
        this.lineTypeId = lineTypeId;
        return this;
    }

    public void setLineTypeId(Long lineTypeId) {
        this.lineTypeId = lineTypeId;
    }

    public Set<TreatyDetails> getIds() {
        return ids;
    }

    public Treaty ids(Set<TreatyDetails> treatyDetails) {
        this.ids = treatyDetails;
        return this;
    }

    public Treaty addId(TreatyDetails treatyDetails) {
        this.ids.add(treatyDetails);
        treatyDetails.setTreatyId(this);
        return this;
    }

    public Treaty removeId(TreatyDetails treatyDetails) {
        this.ids.remove(treatyDetails);
        treatyDetails.setTreatyId(null);
        return this;
    }

    public void setIds(Set<TreatyDetails> treatyDetails) {
        this.ids = treatyDetails;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Treaty)) {
            return false;
        }
        return id != null && id.equals(((Treaty) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Treaty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lineTypeId=" + getLineTypeId() +
            "}";
    }
}
