package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductsSectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductsSection.class);
        ProductsSection productsSection1 = new ProductsSection();
        productsSection1.setId(1L);
        ProductsSection productsSection2 = new ProductsSection();
        productsSection2.setId(productsSection1.getId());
        assertThat(productsSection1).isEqualTo(productsSection2);
        productsSection2.setId(2L);
        assertThat(productsSection1).isNotEqualTo(productsSection2);
        productsSection1.setId(null);
        assertThat(productsSection1).isNotEqualTo(productsSection2);
    }
}
