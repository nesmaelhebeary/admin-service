package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeductiblesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deductibles.class);
        Deductibles deductibles1 = new Deductibles();
        deductibles1.setId(1L);
        Deductibles deductibles2 = new Deductibles();
        deductibles2.setId(deductibles1.getId());
        assertThat(deductibles1).isEqualTo(deductibles2);
        deductibles2.setId(2L);
        assertThat(deductibles1).isNotEqualTo(deductibles2);
        deductibles1.setId(null);
        assertThat(deductibles1).isNotEqualTo(deductibles2);
    }
}
