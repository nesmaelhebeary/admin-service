package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.LookupTypes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LookupTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LookupTypesRepository extends JpaRepository<LookupTypes, Long> {}
