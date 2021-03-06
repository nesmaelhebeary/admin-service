package com.axa.hypercell.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.axa.hypercell.admin.web.rest.TestUtil;

public class ProductClausesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductClauses.class);
        ProductClauses productClauses1 = new ProductClauses();
        productClauses1.setId(1L);
        ProductClauses productClauses2 = new ProductClauses();
        productClauses2.setId(productClauses1.getId());
        assertThat(productClauses1).isEqualTo(productClauses2);
        productClauses2.setId(2L);
        assertThat(productClauses1).isNotEqualTo(productClauses2);
        productClauses1.setId(null);
        assertThat(productClauses1).isNotEqualTo(productClauses2);
    }
}
