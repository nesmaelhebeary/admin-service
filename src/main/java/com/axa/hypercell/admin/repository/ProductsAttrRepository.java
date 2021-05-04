package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ProductsAttr;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductsAttr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductsAttrRepository extends JpaRepository<ProductsAttr, Long> {}
