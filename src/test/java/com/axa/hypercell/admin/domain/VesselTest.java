package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VesselTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vessel.class);
        Vessel vessel1 = new Vessel();
        vessel1.setId(1L);
        Vessel vessel2 = new Vessel();
        vessel2.setId(vessel1.getId());
        assertThat(vessel1).isEqualTo(vessel2);
        vessel2.setId(2L);
        assertThat(vessel1).isNotEqualTo(vessel2);
        vessel1.setId(null);
        assertThat(vessel1).isNotEqualTo(vessel2);
    }
}
