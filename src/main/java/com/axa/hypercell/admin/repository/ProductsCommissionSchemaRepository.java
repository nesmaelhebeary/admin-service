package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ProductsCommissionSchema;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductsCommissionSchema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductsCommissionSchemaRepository extends JpaRepository<ProductsCommissionSchema, Long> {}
