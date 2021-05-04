package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExtensionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Extensions.class);
        Extensions extensions1 = new Extensions();
        extensions1.setId(1L);
        Extensions extensions2 = new Extensions();
        extensions2.setId(extensions1.getId());
        assertThat(extensions1).isEqualTo(extensions2);
        extensions2.setId(2L);
        assertThat(extensions1).isNotEqualTo(extensions2);
        extensions1.setId(null);
        assertThat(extensions1).isNotEqualTo(extensions2);
    }
}
