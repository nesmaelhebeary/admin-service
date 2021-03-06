package com.axa.hypercell.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.axa.hypercell.admin.web.rest.TestUtil;

public class ProductTAndCTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductTAndC.class);
        ProductTAndC productTAndC1 = new ProductTAndC();
        productTAndC1.setId(1L);
        ProductTAndC productTAndC2 = new ProductTAndC();
        productTAndC2.setId(productTAndC1.getId());
        assertThat(productTAndC1).isEqualTo(productTAndC2);
        productTAndC2.setId(2L);
        assertThat(productTAndC1).isNotEqualTo(productTAndC2);
        productTAndC1.setId(null);
        assertThat(productTAndC1).isNotEqualTo(productTAndC2);
    }
}
