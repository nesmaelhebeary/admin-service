package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LkExtensionParametersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LkExtensionParameters.class);
        LkExtensionParameters lkExtensionParameters1 = new LkExtensionParameters();
        lkExtensionParameters1.setId(1L);
        LkExtensionParameters lkExtensionParameters2 = new LkExtensionParameters();
        lkExtensionParameters2.setId(lkExtensionParameters1.getId());
        assertThat(lkExtensionParameters1).isEqualTo(lkExtensionParameters2);
        lkExtensionParameters2.setId(2L);
        assertThat(lkExtensionParameters1).isNotEqualTo(lkExtensionParameters2);
        lkExtensionParameters1.setId(null);
        assertThat(lkExtensionParameters1).isNotEqualTo(lkExtensionParameters2);
    }
}
