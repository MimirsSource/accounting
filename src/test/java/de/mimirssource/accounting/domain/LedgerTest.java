package de.mimirssource.accounting.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.mimirssource.accounting.web.rest.TestUtil;

public class LedgerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ledger.class);
        Ledger ledger1 = new Ledger();
        ledger1.setId(1L);
        Ledger ledger2 = new Ledger();
        ledger2.setId(ledger1.getId());
        assertThat(ledger1).isEqualTo(ledger2);
        ledger2.setId(2L);
        assertThat(ledger1).isNotEqualTo(ledger2);
        ledger1.setId(null);
        assertThat(ledger1).isNotEqualTo(ledger2);
    }
}
