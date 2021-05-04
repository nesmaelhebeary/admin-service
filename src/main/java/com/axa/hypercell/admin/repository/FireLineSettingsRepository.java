package com.axa.hypercell.admin.repository;

import com.axa.hypercell.admin.domain.FireLineSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FireLineSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FireLineSettingsRepository extends JpaRepository<FireLineSettings, Long> {}
