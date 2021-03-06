package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Roof;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Roof entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoofRepository extends JpaRepository<Roof, Long> {
}
