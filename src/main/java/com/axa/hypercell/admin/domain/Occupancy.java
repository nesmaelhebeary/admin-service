package com.axa.hypercell.admin.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Occupancy.
 */
@Entity
@Table(name = "occupancy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Occupancy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "description")
    private String description;

    @Column(name = "line_type_id")
    private String lineTypeId;

    @Column(name = "line_type_occupancy")
    private String lineTypeOccupancy;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Occupancy id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Occupancy name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Occupancy shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return this.description;
    }

    public Occupancy description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLineTypeId() {
        return this.lineTypeId;
    }

    public Occupancy lineTypeId(String lineTypeId) {
        this.lineTypeId = lineTypeId;
        return this;
    }

    public void setLineTypeId(String lineTypeId) {
        this.lineTypeId = lineTypeId;
    }

    public String getLineTypeOccupancy() {
        return this.lineTypeOccupancy;
    }

    public Occupancy lineTypeOccupancy(String lineTypeOccupancy) {
        this.lineTypeOccupancy = lineTypeOccupancy;
        return this;
    }

    public void setLineTypeOccupancy(String lineTypeOccupancy) {
        this.lineTypeOccupancy = lineTypeOccupancy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Occupancy)) {
            return false;
        }
        return id != null && id.equals(((Occupancy) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Occupancy{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", description='" + getDescription() + "'" +
            ", lineTypeId='" + getLineTypeId() + "'" +
            ", lineTypeOccupancy='" + getLineTypeOccupancy() + "'" +
            "}";
    }
}
