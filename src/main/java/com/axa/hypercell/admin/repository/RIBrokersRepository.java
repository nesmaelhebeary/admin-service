package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.RIBrokers;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RIBrokers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RIBrokersRepository extends JpaRepository<RIBrokers, Long> {
}
