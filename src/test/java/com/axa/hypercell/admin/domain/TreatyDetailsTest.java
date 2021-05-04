package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TreatyDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TreatyDetails.class);
        TreatyDetails treatyDetails1 = new TreatyDetails();
        treatyDetails1.setId(1L);
        TreatyDetails treatyDetails2 = new TreatyDetails();
        treatyDetails2.setId(treatyDetails1.getId());
        assertThat(treatyDetails1).isEqualTo(treatyDetails2);
        treatyDetails2.setId(2L);
        assertThat(treatyDetails1).isNotEqualTo(treatyDetails2);
        treatyDetails1.setId(null);
        assertThat(treatyDetails1).isNotEqualTo(treatyDetails2);
    }
}
