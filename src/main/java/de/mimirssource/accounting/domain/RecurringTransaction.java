package de.mimirssource.accounting.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A RecurringTransaction.
 */
@Entity
@Table(name = "recurring_transaction")
public class RecurringTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @NotNull
    @Column(name = "date_of_month", nullable = false)
    private Integer dateOfMonth;

    @OneToOne
    @JoinColumn(unique = true)
    private Ledger fromLedger;

    @OneToOne
    @JoinColumn(unique = true)
    private Ledger toLedger;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RecurringTransaction name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public RecurringTransaction amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getDateOfMonth() {
        return dateOfMonth;
    }

    public RecurringTransaction dateOfMonth(Integer dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
        return this;
    }

    public void setDateOfMonth(Integer dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
    }

    public Ledger getFromLedger() {
        return fromLedger;
    }

    public RecurringTransaction fromLedger(Ledger ledger) {
        this.fromLedger = ledger;
        return this;
    }

    public void setFromLedger(Ledger ledger) {
        this.fromLedger = ledger;
    }

    public Ledger getToLedger() {
        return toLedger;
    }

    public RecurringTransaction toLedger(Ledger ledger) {
        this.toLedger = ledger;
        return this;
    }

    public void setToLedger(Ledger ledger) {
        this.toLedger = ledger;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecurringTransaction)) {
            return false;
        }
        return id != null && id.equals(((RecurringTransaction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RecurringTransaction{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amount=" + getAmount() +
            ", dateOfMonth=" + getDateOfMonth() +
            "}";
    }
}
