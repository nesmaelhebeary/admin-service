package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoofTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Roof.class);
        Roof roof1 = new Roof();
        roof1.setId(1L);
        Roof roof2 = new Roof();
        roof2.setId(roof1.getId());
        assertThat(roof1).isEqualTo(roof2);
        roof2.setId(2L);
        assertThat(roof1).isNotEqualTo(roof2);
        roof1.setId(null);
        assertThat(roof1).isNotEqualTo(roof2);
    }
}
