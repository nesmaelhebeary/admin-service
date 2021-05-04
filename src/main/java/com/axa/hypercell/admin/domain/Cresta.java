package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cresta.
 */
@Entity
@Table(name = "cresta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cresta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "crestaId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "crestaId" }, allowSetters = true)
    private Set<SubArea> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cresta id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Cresta name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Cresta shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return this.code;
    }

    public Cresta code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<SubArea> getIds() {
        return this.ids;
    }

    public Cresta ids(Set<SubArea> subAreas) {
        this.setIds(subAreas);
        return this;
    }

    public Cresta addId(SubArea subArea) {
        this.ids.add(subArea);
        subArea.setCrestaId(this);
        return this;
    }

    public Cresta removeId(SubArea subArea) {
        this.ids.remove(subArea);
        subArea.setCrestaId(null);
        return this;
    }

    public void setIds(Set<SubArea> subAreas) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setCrestaId(null));
        }
        if (subAreas != null) {
            subAreas.forEach(i -> i.setCrestaId(this));
        }
        this.ids = subAreas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cresta)) {
            return false;
        }
        return id != null && id.equals(((Cresta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cresta{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
