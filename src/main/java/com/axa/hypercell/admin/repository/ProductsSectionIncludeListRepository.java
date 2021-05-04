package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ProductsSectionIncludeList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductsSectionIncludeList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductsSectionIncludeListRepository extends JpaRepository<ProductsSectionIncludeList, Long> {}
