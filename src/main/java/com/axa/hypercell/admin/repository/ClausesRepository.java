package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Clauses;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Clauses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClausesRepository extends JpaRepository<Clauses, Long> {
}
