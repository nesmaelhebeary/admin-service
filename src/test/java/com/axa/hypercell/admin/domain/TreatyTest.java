package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TreatyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Treaty.class);
        Treaty treaty1 = new Treaty();
        treaty1.setId(1L);
        Treaty treaty2 = new Treaty();
        treaty2.setId(treaty1.getId());
        assertThat(treaty1).isEqualTo(treaty2);
        treaty2.setId(2L);
        assertThat(treaty1).isNotEqualTo(treaty2);
        treaty1.setId(null);
        assertThat(treaty1).isNotEqualTo(treaty2);
    }
}
