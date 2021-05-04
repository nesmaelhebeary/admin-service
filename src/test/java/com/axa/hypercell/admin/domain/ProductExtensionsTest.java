package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductExtensionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductExtensions.class);
        ProductExtensions productExtensions1 = new ProductExtensions();
        productExtensions1.setId(1L);
        ProductExtensions productExtensions2 = new ProductExtensions();
        productExtensions2.setId(productExtensions1.getId());
        assertThat(productExtensions1).isEqualTo(productExtensions2);
        productExtensions2.setId(2L);
        assertThat(productExtensions1).isNotEqualTo(productExtensions2);
        productExtensions1.setId(null);
        assertThat(productExtensions1).isNotEqualTo(productExtensions2);
    }
}
