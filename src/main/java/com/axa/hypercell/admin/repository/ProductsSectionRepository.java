package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ProductsSection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductsSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductsSectionRepository extends JpaRepository<ProductsSection, Long> {}
