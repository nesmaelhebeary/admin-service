package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Extensions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Extensions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtensionsRepository extends JpaRepository<Extensions, Long> {}
