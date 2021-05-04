package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FireLineSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FireLineSettings.class);
        FireLineSettings fireLineSettings1 = new FireLineSettings();
        fireLineSettings1.setId(1L);
        FireLineSettings fireLineSettings2 = new FireLineSettings();
        fireLineSettings2.setId(fireLineSettings1.getId());
        assertThat(fireLineSettings1).isEqualTo(fireLineSettings2);
        fireLineSettings2.setId(2L);
        assertThat(fireLineSettings1).isNotEqualTo(fireLineSettings2);
        fireLineSettings1.setId(null);
        assertThat(fireLineSettings1).isNotEqualTo(fireLineSettings2);
    }
}
