package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.NaceCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NaceCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NaceCodeRepository extends JpaRepository<NaceCode, Long> {}
