package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.TermsAndConditions;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TermsAndConditions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TermsAndConditionsRepository extends JpaRepository<TermsAndConditions, Long> {
}
