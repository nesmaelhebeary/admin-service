package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LkClausesParametersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LkClausesParameters.class);
        LkClausesParameters lkClausesParameters1 = new LkClausesParameters();
        lkClausesParameters1.setId(1L);
        LkClausesParameters lkClausesParameters2 = new LkClausesParameters();
        lkClausesParameters2.setId(lkClausesParameters1.getId());
        assertThat(lkClausesParameters1).isEqualTo(lkClausesParameters2);
        lkClausesParameters2.setId(2L);
        assertThat(lkClausesParameters1).isNotEqualTo(lkClausesParameters2);
        lkClausesParameters1.setId(null);
        assertThat(lkClausesParameters1).isNotEqualTo(lkClausesParameters2);
    }
}
