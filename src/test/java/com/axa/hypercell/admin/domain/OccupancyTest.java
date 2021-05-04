package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OccupancyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Occupancy.class);
        Occupancy occupancy1 = new Occupancy();
        occupancy1.setId(1L);
        Occupancy occupancy2 = new Occupancy();
        occupancy2.setId(occupancy1.getId());
        assertThat(occupancy1).isEqualTo(occupancy2);
        occupancy2.setId(2L);
        assertThat(occupancy1).isNotEqualTo(occupancy2);
        occupancy1.setId(null);
        assertThat(occupancy1).isNotEqualTo(occupancy2);
    }
}
