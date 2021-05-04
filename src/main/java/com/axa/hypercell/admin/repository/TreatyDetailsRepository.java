package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.TreatyDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TreatyDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TreatyDetailsRepository extends JpaRepository<TreatyDetails, Long> {}
