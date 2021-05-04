package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductClauseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductClause.class);
        ProductClause productClause1 = new ProductClause();
        productClause1.setId(1L);
        ProductClause productClause2 = new ProductClause();
        productClause2.setId(productClause1.getId());
        assertThat(productClause1).isEqualTo(productClause2);
        productClause2.setId(2L);
        assertThat(productClause1).isNotEqualTo(productClause2);
        productClause1.setId(null);
        assertThat(productClause1).isNotEqualTo(productClause2);
    }
}
