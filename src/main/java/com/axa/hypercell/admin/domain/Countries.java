package com.axa.hypercell.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Countries.
 */
@Entity
@Table(name = "countries")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Countries implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_english")
    private String nameEnglish;

    @Column(name = "name_arabic")
    private String nameArabic;

    @OneToMany(mappedBy = "countryId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "countryId" }, allowSetters = true)
    private Set<Port> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Countries id(Long id) {
        this.id = id;
        return this;
    }

    public String getNameEnglish() {
        return this.nameEnglish;
    }

    public Countries nameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
        return this;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public String getNameArabic() {
        return this.nameArabic;
    }

    public Countries nameArabic(String nameArabic) {
        this.nameArabic = nameArabic;
        return this;
    }

    public void setNameArabic(String nameArabic) {
        this.nameArabic = nameArabic;
    }

    public Set<Port> getIds() {
        return this.ids;
    }

    public Countries ids(Set<Port> ports) {
        this.setIds(ports);
        return this;
    }

    public Countries addId(Port port) {
        this.ids.add(port);
        port.setCountryId(this);
        return this;
    }

    public Countries removeId(Port port) {
        this.ids.remove(port);
        port.setCountryId(null);
        return this;
    }

    public void setIds(Set<Port> ports) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setCountryId(null));
        }
        if (ports != null) {
            ports.forEach(i -> i.setCountryId(this));
        }
        this.ids = ports;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Countries)) {
            return false;
        }
        return id != null && id.equals(((Countries) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Countries{" +
            "id=" + getId() +
            ", nameEnglish='" + getNameEnglish() + "'" +
            ", nameArabic='" + getNameArabic() + "'" +
            "}";
    }
}
