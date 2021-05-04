package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExtensionParametersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtensionParameters.class);
        ExtensionParameters extensionParameters1 = new ExtensionParameters();
        extensionParameters1.setId(1L);
        ExtensionParameters extensionParameters2 = new ExtensionParameters();
        extensionParameters2.setId(extensionParameters1.getId());
        assertThat(extensionParameters1).isEqualTo(extensionParameters2);
        extensionParameters2.setId(2L);
        assertThat(extensionParameters1).isNotEqualTo(extensionParameters2);
        extensionParameters1.setId(null);
        assertThat(extensionParameters1).isNotEqualTo(extensionParameters2);
    }
}
