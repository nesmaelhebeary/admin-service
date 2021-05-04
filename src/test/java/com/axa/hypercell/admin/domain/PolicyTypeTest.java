package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PolicyTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PolicyType.class);
        PolicyType policyType1 = new PolicyType();
        policyType1.setId(1L);
        PolicyType policyType2 = new PolicyType();
        policyType2.setId(policyType1.getId());
        assertThat(policyType1).isEqualTo(policyType2);
        policyType2.setId(2L);
        assertThat(policyType1).isNotEqualTo(policyType2);
        policyType1.setId(null);
        assertThat(policyType1).isNotEqualTo(policyType2);
    }
}
