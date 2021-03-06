package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.EvaluationBasis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EvaluationBasis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EvaluationBasisRepository extends JpaRepository<EvaluationBasis, Long> {}
