package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductsAttrTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductsAttr.class);
        ProductsAttr productsAttr1 = new ProductsAttr();
        productsAttr1.setId(1L);
        ProductsAttr productsAttr2 = new ProductsAttr();
        productsAttr2.setId(productsAttr1.getId());
        assertThat(productsAttr1).isEqualTo(productsAttr2);
        productsAttr2.setId(2L);
        assertThat(productsAttr1).isNotEqualTo(productsAttr2);
        productsAttr1.setId(null);
        assertThat(productsAttr1).isNotEqualTo(productsAttr2);
    }
}
