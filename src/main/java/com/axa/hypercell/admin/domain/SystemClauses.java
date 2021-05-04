package com.axa.hypercell.admin.domain;

import com.axa.hypercell.admin.domain.enumeration.ClauseType;
import com.axa.hypercell.admin.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SystemClauses.
 */
@Entity
@Table(name = "system_clauses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemClauses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "text_en")
    private String textEn;

    @Column(name = "text_ar")
    private String textAr;

    @Enumerated(EnumType.STRING)
    @Column(name = "clause_type")
    private ClauseType clauseType;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "clauseId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productId", "clauseId" }, allowSetters = true)
    private Set<ProductClause> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SystemClauses id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public SystemClauses code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTextEn() {
        return this.textEn;
    }

    public SystemClauses textEn(String textEn) {
        this.textEn = textEn;
        return this;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public String getTextAr() {
        return this.textAr;
    }

    public SystemClauses textAr(String textAr) {
        this.textAr = textAr;
        return this;
    }

    public void setTextAr(String textAr) {
        this.textAr = textAr;
    }

    public ClauseType getClauseType() {
        return this.clauseType;
    }

    public SystemClauses clauseType(ClauseType clauseType) {
        this.clauseType = clauseType;
        return this;
    }

    public void setClauseType(ClauseType clauseType) {
        this.clauseType = clauseType;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public SystemClauses effectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public SystemClauses status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<ProductClause> getIds() {
        return this.ids;
    }

    public SystemClauses ids(Set<ProductClause> productClauses) {
        this.setIds(productClauses);
        return this;
    }

    public SystemClauses addId(ProductClause productClause) {
        this.ids.add(productClause);
        productClause.setClauseId(this);
        return this;
    }

    public SystemClauses removeId(ProductClause productClause) {
        this.ids.remove(productClause);
        productClause.setClauseId(null);
        return this;
    }

    public void setIds(Set<ProductClause> productClauses) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setClauseId(null));
        }
        if (productClauses != null) {
            productClauses.forEach(i -> i.setClauseId(this));
        }
        this.ids = productClauses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemClauses)) {
            return false;
        }
        return id != null && id.equals(((SystemClauses) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemClauses{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", textEn='" + getTextEn() + "'" +
            ", textAr='" + getTextAr() + "'" +
            ", clauseType='" + getClauseType() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
