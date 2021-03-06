package com.axa.hypercell.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.axa.hypercell.admin.web.rest.TestUtil;

public class RIBrokersTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RIBrokers.class);
        RIBrokers rIBrokers1 = new RIBrokers();
        rIBrokers1.setId(1L);
        RIBrokers rIBrokers2 = new RIBrokers();
        rIBrokers2.setId(rIBrokers1.getId());
        assertThat(rIBrokers1).isEqualTo(rIBrokers2);
        rIBrokers2.setId(2L);
        assertThat(rIBrokers1).isNotEqualTo(rIBrokers2);
        rIBrokers1.setId(null);
        assertThat(rIBrokers1).isNotEqualTo(rIBrokers2);
    }
}
