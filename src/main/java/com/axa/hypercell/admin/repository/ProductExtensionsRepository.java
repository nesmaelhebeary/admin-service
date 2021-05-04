package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ProductExtensions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductExtensions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductExtensionsRepository extends JpaRepository<ProductExtensions, Long> {}
