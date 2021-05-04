package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LineTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineType.class);
        LineType lineType1 = new LineType();
        lineType1.setId(1L);
        LineType lineType2 = new LineType();
        lineType2.setId(lineType1.getId());
        assertThat(lineType1).isEqualTo(lineType2);
        lineType2.setId(2L);
        assertThat(lineType1).isNotEqualTo(lineType2);
        lineType1.setId(null);
        assertThat(lineType1).isNotEqualTo(lineType2);
    }
}
