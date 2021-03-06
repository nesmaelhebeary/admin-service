package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ProductClauses;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProductClauses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductClausesRepository extends JpaRepository<ProductClauses, Long> {
}
