package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubAreaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubArea.class);
        SubArea subArea1 = new SubArea();
        subArea1.setId(1L);
        SubArea subArea2 = new SubArea();
        subArea2.setId(subArea1.getId());
        assertThat(subArea1).isEqualTo(subArea2);
        subArea2.setId(2L);
        assertThat(subArea1).isNotEqualTo(subArea2);
        subArea1.setId(null);
        assertThat(subArea1).isNotEqualTo(subArea2);
    }
}
