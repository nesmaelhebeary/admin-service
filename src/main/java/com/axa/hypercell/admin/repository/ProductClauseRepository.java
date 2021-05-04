package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ProductClause;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductClause entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductClauseRepository extends JpaRepository<ProductClause, Long> {}
