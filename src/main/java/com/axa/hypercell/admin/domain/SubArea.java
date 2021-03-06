package com.axa.hypercell.admin.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A SubArea.
 */
@Entity
@Table(name = "sub_area")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cresta_id")
    private Long crestaId;

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

    public SubArea name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCrestaId() {
        return crestaId;
    }

    public SubArea crestaId(Long crestaId) {
        this.crestaId = crestaId;
        return this;
    }

    public void setCrestaId(Long crestaId) {
        this.crestaId = crestaId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubArea)) {
            return false;
        }
        return id != null && id.equals(((SubArea) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubArea{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", crestaId=" + getCrestaId() +
            "}";
    }
}
