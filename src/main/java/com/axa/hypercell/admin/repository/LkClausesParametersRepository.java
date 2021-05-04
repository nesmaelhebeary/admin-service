package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.LkClausesParameters;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LkClausesParameters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LkClausesParametersRepository extends JpaRepository<LkClausesParameters, Long> {}
