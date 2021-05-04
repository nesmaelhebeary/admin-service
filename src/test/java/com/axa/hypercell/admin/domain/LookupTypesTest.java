package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LookupTypesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LookupTypes.class);
        LookupTypes lookupTypes1 = new LookupTypes();
        lookupTypes1.setId(1L);
        LookupTypes lookupTypes2 = new LookupTypes();
        lookupTypes2.setId(lookupTypes1.getId());
        assertThat(lookupTypes1).isEqualTo(lookupTypes2);
        lookupTypes2.setId(2L);
        assertThat(lookupTypes1).isNotEqualTo(lookupTypes2);
        lookupTypes1.setId(null);
        assertThat(lookupTypes1).isNotEqualTo(lookupTypes2);
    }
}
