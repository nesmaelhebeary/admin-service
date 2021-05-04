package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.ExtensionParameters;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExtensionParameters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtensionParametersRepository extends JpaRepository<ExtensionParameters, Long> {}
