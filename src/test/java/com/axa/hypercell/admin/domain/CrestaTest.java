package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CrestaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cresta.class);
        Cresta cresta1 = new Cresta();
        cresta1.setId(1L);
        Cresta cresta2 = new Cresta();
        cresta2.setId(cresta1.getId());
        assertThat(cresta1).isEqualTo(cresta2);
        cresta2.setId(2L);
        assertThat(cresta1).isNotEqualTo(cresta2);
        cresta1.setId(null);
        assertThat(cresta1).isNotEqualTo(cresta2);
    }
}
