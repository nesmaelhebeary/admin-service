package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Cresta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Cresta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CrestaRepository extends JpaRepository<Cresta, Long> {
}
