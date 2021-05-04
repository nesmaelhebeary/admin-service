package com.axa.hypercell.admin.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Packing.
 */
@Entity
@Table(name = "packing")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Packing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_english")
    private String nameEnglish;

    @Column(name = "name_arabic")
    private String nameArabic;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Packing id(Long id) {
        this.id = id;
        return this;
    }

    public String getNameEnglish() {
        return this.nameEnglish;
    }

    public Packing nameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
        return this;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public String getNameArabic() {
        return this.nameArabic;
    }

    public Packing nameArabic(String nameArabic) {
        this.nameArabic = nameArabic;
        return this;
    }

    public void setNameArabic(String nameArabic) {
        this.nameArabic = nameArabic;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Packing)) {
            return false;
        }
        return id != null && id.equals(((Packing) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Packing{" +
            "id=" + getId() +
            ", nameEnglish='" + getNameEnglish() + "'" +
            ", nameArabic='" + getNameArabic() + "'" +
            "}";
    }
}
