package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.SalesBrokers;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SalesBrokers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesBrokersRepository extends JpaRepository<SalesBrokers, Long> {
}
