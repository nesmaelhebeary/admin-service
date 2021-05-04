package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.LineType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LineType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineTypeRepository extends JpaRepository<LineType, Long> {}
