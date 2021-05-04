package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.CoverType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CoverType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoverTypeRepository extends JpaRepository<CoverType, Long> {}
