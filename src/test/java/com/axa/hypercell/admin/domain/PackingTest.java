package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PackingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Packing.class);
        Packing packing1 = new Packing();
        packing1.setId(1L);
        Packing packing2 = new Packing();
        packing2.setId(packing1.getId());
        assertThat(packing1).isEqualTo(packing2);
        packing2.setId(2L);
        assertThat(packing1).isNotEqualTo(packing2);
        packing1.setId(null);
        assertThat(packing1).isNotEqualTo(packing2);
    }
}
