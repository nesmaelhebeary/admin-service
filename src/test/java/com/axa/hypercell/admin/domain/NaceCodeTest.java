package com.axa.hypercell.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.axa.hypercell.admin.web.rest.TestUtil;

public class NaceCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NaceCode.class);
        NaceCode naceCode1 = new NaceCode();
        naceCode1.setId(1L);
        NaceCode naceCode2 = new NaceCode();
        naceCode2.setId(naceCode1.getId());
        assertThat(naceCode1).isEqualTo(naceCode2);
        naceCode2.setId(2L);
        assertThat(naceCode1).isNotEqualTo(naceCode2);
        naceCode1.setId(null);
        assertThat(naceCode1).isNotEqualTo(naceCode2);
    }
}
