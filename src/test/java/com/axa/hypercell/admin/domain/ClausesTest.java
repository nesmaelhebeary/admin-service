package com.axa.hypercell.admin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.axa.hypercell.admin.web.rest.TestUtil;

public class ClausesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clauses.class);
        Clauses clauses1 = new Clauses();
        clauses1.setId(1L);
        Clauses clauses2 = new Clauses();
        clauses2.setId(clauses1.getId());
        assertThat(clauses1).isEqualTo(clauses2);
        clauses2.setId(2L);
        assertThat(clauses1).isNotEqualTo(clauses2);
        clauses1.setId(null);
        assertThat(clauses1).isNotEqualTo(clauses2);
    }
}
