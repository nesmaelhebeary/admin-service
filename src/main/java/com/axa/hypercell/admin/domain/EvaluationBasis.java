package com.axa.hypercell.admin.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EvaluationBasis.
 */
@Entity
@Table(name = "evaluation_basis")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EvaluationBasis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "naem_en")
    private String naemEn;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EvaluationBasis id(Long id) {
        this.id = id;
        return this;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public EvaluationBasis nameAr(String nameAr) {
        this.nameAr = nameAr;
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNaemEn() {
        return this.naemEn;
    }

    public EvaluationBasis naemEn(String naemEn) {
        this.naemEn = naemEn;
        return this;
    }

    public void setNaemEn(String naemEn) {
        this.naemEn = naemEn;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvaluationBasis)) {
            return false;
        }
        return id != null && id.equals(((EvaluationBasis) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationBasis{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", naemEn='" + getNaemEn() + "'" +
            "}";
    }
}
