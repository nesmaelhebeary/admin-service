package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Packing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Packing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackingRepository extends JpaRepository<Packing, Long> {}
