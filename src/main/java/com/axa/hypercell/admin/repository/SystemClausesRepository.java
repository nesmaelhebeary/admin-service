package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.SystemClauses;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SystemClauses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemClausesRepository extends JpaRepository<SystemClauses, Long> {}
