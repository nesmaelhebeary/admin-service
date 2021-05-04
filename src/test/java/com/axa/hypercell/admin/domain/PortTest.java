package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PortTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Port.class);
        Port port1 = new Port();
        port1.setId(1L);
        Port port2 = new Port();
        port2.setId(port1.getId());
        assertThat(port1).isEqualTo(port2);
        port2.setId(2L);
        assertThat(port1).isNotEqualTo(port2);
        port1.setId(null);
        assertThat(port1).isNotEqualTo(port2);
    }
}
