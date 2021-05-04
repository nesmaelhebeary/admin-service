package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.PolicyType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PolicyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PolicyTypeRepository extends JpaRepository<PolicyType, Long> {}
