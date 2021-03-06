package com.axa.hypercell.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.axa.hypercell.admin.web.rest.TestUtil;

public class SalesBrokersTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesBrokers.class);
        SalesBrokers salesBrokers1 = new SalesBrokers();
        salesBrokers1.setId(1L);
        SalesBrokers salesBrokers2 = new SalesBrokers();
        salesBrokers2.setId(salesBrokers1.getId());
        assertThat(salesBrokers1).isEqualTo(salesBrokers2);
        salesBrokers2.setId(2L);
        assertThat(salesBrokers1).isNotEqualTo(salesBrokers2);
        salesBrokers1.setId(null);
        assertThat(salesBrokers1).isNotEqualTo(salesBrokers2);
    }
}
