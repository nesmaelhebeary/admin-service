package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Vessel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vessel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VesselRepository extends JpaRepository<Vessel, Long> {}
