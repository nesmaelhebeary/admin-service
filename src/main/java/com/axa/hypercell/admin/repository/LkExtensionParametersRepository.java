package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.LkExtensionParameters;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LkExtensionParameters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LkExtensionParametersRepository extends JpaRepository<LkExtensionParameters, Long> {}
