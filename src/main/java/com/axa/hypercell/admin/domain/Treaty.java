package com.axa.hypercell.admin.domain;

import com.axa.hypercell.admin.domain.enumeration.TreatyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "treaty_type")
    private TreatyType treatyType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "treaty_document_path")
    private String treatyDocumentPath;

    @OneToMany(mappedBy = "treatyId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "treatyId" }, allowSetters = true)
    private Set<TreatyDetails> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Treaty id(Long id) {
        this.id = id;
        return this;
    }

    public TreatyType getTreatyType() {
        return this.treatyType;
    }

    public Treaty treatyType(TreatyType treatyType) {
        this.treatyType = treatyType;
        return this;
    }

    public void setTreatyType(TreatyType treatyType) {
        this.treatyType = treatyType;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Treaty startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Treaty endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTreatyDocumentPath() {
        return this.treatyDocumentPath;
    }

    public Treaty treatyDocumentPath(String treatyDocumentPath) {
        this.treatyDocumentPath = treatyDocumentPath;
        return this;
    }

    public void setTreatyDocumentPath(String treatyDocumentPath) {
        this.treatyDocumentPath = treatyDocumentPath;
    }

    public Set<TreatyDetails> getIds() {
        return this.ids;
    }

    public Treaty ids(Set<TreatyDetails> treatyDetails) {
        this.setIds(treatyDetails);
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
        if (this.ids != null) {
            this.ids.forEach(i -> i.setTreatyId(null));
        }
        if (treatyDetails != null) {
            treatyDetails.forEach(i -> i.setTreatyId(this));
        }
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Treaty{" +
            "id=" + getId() +
            ", treatyType='" + getTreatyType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", treatyDocumentPath='" + getTreatyDocumentPath() + "'" +
            "}";
    }
}
