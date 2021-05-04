package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @Column(name = "code")
    private String code;

    @Column(name = "cresta_id")
    private Long crestaId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids" }, allowSetters = true)
    private Cresta crestaId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubArea id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SubArea name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public SubArea code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCrestaId() {
        return this.crestaId;
    }

    public SubArea crestaId(Long crestaId) {
        this.crestaId = crestaId;
        return this;
    }

    public void setCrestaId(Long crestaId) {
        this.crestaId = crestaId;
    }

    public Cresta getCrestaId() {
        return this.crestaId;
    }

    public SubArea crestaId(Cresta cresta) {
        this.setCrestaId(cresta);
        return this;
    }

    public void setCrestaId(Cresta cresta) {
        this.crestaId = cresta;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubArea{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", crestaId=" + getCrestaId() +
            "}";
    }
}
