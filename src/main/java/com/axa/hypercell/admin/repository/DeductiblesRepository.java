package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Deductibles;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Deductibles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeductiblesRepository extends JpaRepository<Deductibles, Long> {}
