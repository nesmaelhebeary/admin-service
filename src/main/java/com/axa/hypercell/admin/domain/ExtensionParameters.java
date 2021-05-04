package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExtensionParameters.
 */
@Entity
@Table(name = "extension_parameters")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExtensionParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parameter_id")
    private Long parameterId;

    @Column(name = "extension_id")
    private Long extensionId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids", "ids" }, allowSetters = true)
    private Extensions extensionId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids" }, allowSetters = true)
    private LkExtensionParameters parameterId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExtensionParameters id(Long id) {
        this.id = id;
        return this;
    }

    public Long getParameterId() {
        return this.parameterId;
    }

    public ExtensionParameters parameterId(Long parameterId) {
        this.parameterId = parameterId;
        return this;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public Long getExtensionId() {
        return this.extensionId;
    }

    public ExtensionParameters extensionId(Long extensionId) {
        this.extensionId = extensionId;
        return this;
    }

    public void setExtensionId(Long extensionId) {
        this.extensionId = extensionId;
    }

    public Extensions getExtensionId() {
        return this.extensionId;
    }

    public ExtensionParameters extensionId(Extensions extensions) {
        this.setExtensionId(extensions);
        return this;
    }

    public void setExtensionId(Extensions extensions) {
        this.extensionId = extensions;
    }

    public LkExtensionParameters getParameterId() {
        return this.parameterId;
    }

    public ExtensionParameters parameterId(LkExtensionParameters lkExtensionParameters) {
        this.setParameterId(lkExtensionParameters);
        return this;
    }

    public void setParameterId(LkExtensionParameters lkExtensionParameters) {
        this.parameterId = lkExtensionParameters;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtensionParameters)) {
            return false;
        }
        return id != null && id.equals(((ExtensionParameters) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtensionParameters{" +
            "id=" + getId() +
            ", parameterId=" + getParameterId() +
            ", extensionId=" + getExtensionId() +
            "}";
    }
}
