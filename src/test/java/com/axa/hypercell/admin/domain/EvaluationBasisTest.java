package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvaluationBasisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvaluationBasis.class);
        EvaluationBasis evaluationBasis1 = new EvaluationBasis();
        evaluationBasis1.setId(1L);
        EvaluationBasis evaluationBasis2 = new EvaluationBasis();
        evaluationBasis2.setId(evaluationBasis1.getId());
        assertThat(evaluationBasis1).isEqualTo(evaluationBasis2);
        evaluationBasis2.setId(2L);
        assertThat(evaluationBasis1).isNotEqualTo(evaluationBasis2);
        evaluationBasis1.setId(null);
        assertThat(evaluationBasis1).isNotEqualTo(evaluationBasis2);
    }
}
