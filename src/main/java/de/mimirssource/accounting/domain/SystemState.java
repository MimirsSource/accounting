package de.mimirssource.accounting.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "system_state")
public class SystemState {


    @Id
    private SystemKey name;

    private Instant date;

    public SystemKey getName() {
        return name;
    }

    public void setName(SystemKey name) {
        this.name = name;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public static SystemState noRecurringTransactionsYet() {
        final SystemState systemState = new SystemState();
        systemState.name = SystemKey.RECURRING_APPLIED_UNTIL;
        systemState.date = Instant.MIN;
        return systemState;
    }

    public enum SystemKey {
        LAST_BALANCE, RECURRING_APPLIED_UNTIL
    }

}
