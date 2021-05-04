package com.axa.hypercell.admin.domain;

import com.axa.hypercell.admin.domain.enumeration.DataType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LkExtensionParameters.
 */
@Entity
@Table(name = "lk_extension_parameters")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LkExtensionParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private DataType dataType;

    @OneToMany(mappedBy = "parameterId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "extensionId", "parameterId" }, allowSetters = true)
    private Set<ExtensionParameters> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LkExtensionParameters id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public LkExtensionParameters name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public LkExtensionParameters dataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Set<ExtensionParameters> getIds() {
        return this.ids;
    }

    public LkExtensionParameters ids(Set<ExtensionParameters> extensionParameters) {
        this.setIds(extensionParameters);
        return this;
    }

    public LkExtensionParameters addId(ExtensionParameters extensionParameters) {
        this.ids.add(extensionParameters);
        extensionParameters.setParameterId(this);
        return this;
    }

    public LkExtensionParameters removeId(ExtensionParameters extensionParameters) {
        this.ids.remove(extensionParameters);
        extensionParameters.setParameterId(null);
        return this;
    }

    public void setIds(Set<ExtensionParameters> extensionParameters) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setParameterId(null));
        }
        if (extensionParameters != null) {
            extensionParameters.forEach(i -> i.setParameterId(this));
        }
        this.ids = extensionParameters;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LkExtensionParameters)) {
            return false;
        }
        return id != null && id.equals(((LkExtensionParameters) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LkExtensionParameters{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dataType='" + getDataType() + "'" +
            "}";
    }
}
