package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemClausesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemClauses.class);
        SystemClauses systemClauses1 = new SystemClauses();
        systemClauses1.setId(1L);
        SystemClauses systemClauses2 = new SystemClauses();
        systemClauses2.setId(systemClauses1.getId());
        assertThat(systemClauses1).isEqualTo(systemClauses2);
        systemClauses2.setId(2L);
        assertThat(systemClauses1).isNotEqualTo(systemClauses2);
        systemClauses1.setId(null);
        assertThat(systemClauses1).isNotEqualTo(systemClauses2);
    }
}
