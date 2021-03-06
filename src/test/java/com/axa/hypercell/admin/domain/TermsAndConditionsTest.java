package com.axa.hypercell.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.axa.hypercell.admin.web.rest.TestUtil;

public class TermsAndConditionsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TermsAndConditions.class);
        TermsAndConditions termsAndConditions1 = new TermsAndConditions();
        termsAndConditions1.setId(1L);
        TermsAndConditions termsAndConditions2 = new TermsAndConditions();
        termsAndConditions2.setId(termsAndConditions1.getId());
        assertThat(termsAndConditions1).isEqualTo(termsAndConditions2);
        termsAndConditions2.setId(2L);
        assertThat(termsAndConditions1).isNotEqualTo(termsAndConditions2);
        termsAndConditions1.setId(null);
        assertThat(termsAndConditions1).isNotEqualTo(termsAndConditions2);
    }
}
