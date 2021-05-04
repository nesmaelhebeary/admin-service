package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ReInsuranceCompany;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReInsuranceCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReInsuranceCompanyRepository extends JpaRepository<ReInsuranceCompany, Long> {}
