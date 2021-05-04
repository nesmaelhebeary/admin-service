package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductsSectionIncludeListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductsSectionIncludeList.class);
        ProductsSectionIncludeList productsSectionIncludeList1 = new ProductsSectionIncludeList();
        productsSectionIncludeList1.setId(1L);
        ProductsSectionIncludeList productsSectionIncludeList2 = new ProductsSectionIncludeList();
        productsSectionIncludeList2.setId(productsSectionIncludeList1.getId());
        assertThat(productsSectionIncludeList1).isEqualTo(productsSectionIncludeList2);
        productsSectionIncludeList2.setId(2L);
        assertThat(productsSectionIncludeList1).isNotEqualTo(productsSectionIncludeList2);
        productsSectionIncludeList1.setId(null);
        assertThat(productsSectionIncludeList1).isNotEqualTo(productsSectionIncludeList2);
    }
}
