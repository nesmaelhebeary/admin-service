package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ProductTAndC;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProductTAndC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductTAndCRepository extends JpaRepository<ProductTAndC, Long> {
}
