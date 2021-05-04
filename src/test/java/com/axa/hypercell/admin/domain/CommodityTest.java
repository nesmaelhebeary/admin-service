package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommodityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commodity.class);
        Commodity commodity1 = new Commodity();
        commodity1.setId(1L);
        Commodity commodity2 = new Commodity();
        commodity2.setId(commodity1.getId());
        assertThat(commodity1).isEqualTo(commodity2);
        commodity2.setId(2L);
        assertThat(commodity1).isNotEqualTo(commodity2);
        commodity1.setId(null);
        assertThat(commodity1).isNotEqualTo(commodity2);
    }
}
