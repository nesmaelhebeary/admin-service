package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Occupancy;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Occupancy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OccupancyRepository extends JpaRepository<Occupancy, Long> {
}
