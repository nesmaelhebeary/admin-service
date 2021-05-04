package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoverTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoverType.class);
        CoverType coverType1 = new CoverType();
        coverType1.setId(1L);
        CoverType coverType2 = new CoverType();
        coverType2.setId(coverType1.getId());
        assertThat(coverType1).isEqualTo(coverType2);
        coverType2.setId(2L);
        assertThat(coverType1).isNotEqualTo(coverType2);
        coverType1.setId(null);
        assertThat(coverType1).isNotEqualTo(coverType2);
    }
}
