package com.axa.hypercell.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.axa.hypercell.admin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductsCommissionSchemaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductsCommissionSchema.class);
        ProductsCommissionSchema productsCommissionSchema1 = new ProductsCommissionSchema();
        productsCommissionSchema1.setId(1L);
        ProductsCommissionSchema productsCommissionSchema2 = new ProductsCommissionSchema();
        productsCommissionSchema2.setId(productsCommissionSchema1.getId());
        assertThat(productsCommissionSchema1).isEqualTo(productsCommissionSchema2);
        productsCommissionSchema2.setId(2L);
        assertThat(productsCommissionSchema1).isNotEqualTo(productsCommissionSchema2);
        productsCommissionSchema1.setId(null);
        assertThat(productsCommissionSchema1).isNotEqualTo(productsCommissionSchema2);
    }
}
