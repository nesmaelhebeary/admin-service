package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Treaty;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Treaty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TreatyRepository extends JpaRepository<Treaty, Long> {
}
