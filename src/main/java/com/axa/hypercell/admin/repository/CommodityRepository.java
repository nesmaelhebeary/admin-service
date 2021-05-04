package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.Commodity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Commodity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommodityRepository extends JpaRepository<Commodity, Long> {}
