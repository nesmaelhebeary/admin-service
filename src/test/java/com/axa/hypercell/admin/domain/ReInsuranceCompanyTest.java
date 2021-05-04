package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReInsuranceCompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReInsuranceCompany.class);
        ReInsuranceCompany reInsuranceCompany1 = new ReInsuranceCompany();
        reInsuranceCompany1.setId(1L);
        ReInsuranceCompany reInsuranceCompany2 = new ReInsuranceCompany();
        reInsuranceCompany2.setId(reInsuranceCompany1.getId());
        assertThat(reInsuranceCompany1).isEqualTo(reInsuranceCompany2);
        reInsuranceCompany2.setId(2L);
        assertThat(reInsuranceCompany1).isNotEqualTo(reInsuranceCompany2);
        reInsuranceCompany1.setId(null);
        assertThat(reInsuranceCompany1).isNotEqualTo(reInsuranceCompany2);
    }
}
