package com.axa.hypercell.admin.domain;

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
 * A Extensions.
 */
@Entity
@Table(name = "extensions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Extensions implements Serializable {

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

    @Column(name = "affect_mpl")
    private String affectMpl;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "extensionId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "extensionId", "parameterId" }, allowSetters = true)
    private Set<ExtensionParameters> ids = new HashSet<>();

    @OneToMany(mappedBy = "extensionId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productId", "extensionId" }, allowSetters = true)
    private Set<ProductExtensions> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Extensions id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Extensions code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTextEn() {
        return this.textEn;
    }

    public Extensions textEn(String textEn) {
        this.textEn = textEn;
        return this;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public String getTextAr() {
        return this.textAr;
    }

    public Extensions textAr(String textAr) {
        this.textAr = textAr;
        return this;
    }

    public void setTextAr(String textAr) {
        this.textAr = textAr;
    }

    public String getAffectMpl() {
        return this.affectMpl;
    }

    public Extensions affectMpl(String affectMpl) {
        this.affectMpl = affectMpl;
        return this;
    }

    public void setAffectMpl(String affectMpl) {
        this.affectMpl = affectMpl;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public Extensions effectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public Extensions status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<ExtensionParameters> getIds() {
        return this.ids;
    }

    public Extensions ids(Set<ExtensionParameters> extensionParameters) {
        this.setIds(extensionParameters);
        return this;
    }

    public Extensions addId(ExtensionParameters extensionParameters) {
        this.ids.add(extensionParameters);
        extensionParameters.setExtensionId(this);
        return this;
    }

    public Extensions removeId(ExtensionParameters extensionParameters) {
        this.ids.remove(extensionParameters);
        extensionParameters.setExtensionId(null);
        return this;
    }

    public void setIds(Set<ExtensionParameters> extensionParameters) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setExtensionId(null));
        }
        if (extensionParameters != null) {
            extensionParameters.forEach(i -> i.setExtensionId(this));
        }
        this.ids = extensionParameters;
    }

    public Set<ProductExtensions> getIds() {
        return this.ids;
    }

    public Extensions ids(Set<ProductExtensions> productExtensions) {
        this.setIds(productExtensions);
        return this;
    }

    public Extensions addId(ProductExtensions productExtensions) {
        this.ids.add(productExtensions);
        productExtensions.setExtensionId(this);
        return this;
    }

    public Extensions removeId(ProductExtensions productExtensions) {
        this.ids.remove(productExtensions);
        productExtensions.setExtensionId(null);
        return this;
    }

    public void setIds(Set<ProductExtensions> productExtensions) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setExtensionId(null));
        }
        if (productExtensions != null) {
            productExtensions.forEach(i -> i.setExtensionId(this));
        }
        this.ids = productExtensions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Extensions)) {
            return false;
        }
        return id != null && id.equals(((Extensions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Extensions{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", textEn='" + getTextEn() + "'" +
            ", textAr='" + getTextAr() + "'" +
            ", affectMpl='" + getAffectMpl() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
