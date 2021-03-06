package com.axa.hypercell.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.axa.hypercell.admin.web.rest.TestUtil;

public class RoofTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Roof.class);
        Roof roof1 = new Roof();
        roof1.setId(1L);
        Roof roof2 = new Roof();
        roof2.setId(roof1.getId());
        assertThat(roof1).isEqualTo(roof2);
        roof2.setId(2L);
        assertThat(roof1).isNotEqualTo(roof2);
        roof1.setId(null);
        assertThat(roof1).isNotEqualTo(roof2);
    }
}
