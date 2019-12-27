package de.mimirssource.accounting.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.mimirssource.accounting.web.rest.TestUtil;

public class RecurringTransactionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecurringTransaction.class);
        RecurringTransaction recurringTransaction1 = new RecurringTransaction();
        recurringTransaction1.setId(1L);
        RecurringTransaction recurringTransaction2 = new RecurringTransaction();
        recurringTransaction2.setId(recurringTransaction1.getId());
        assertThat(recurringTransaction1).isEqualTo(recurringTransaction2);
        recurringTransaction2.setId(2L);
        assertThat(recurringTransaction1).isNotEqualTo(recurringTransaction2);
        recurringTransaction1.setId(null);
        assertThat(recurringTransaction1).isNotEqualTo(recurringTransaction2);
    }
}
