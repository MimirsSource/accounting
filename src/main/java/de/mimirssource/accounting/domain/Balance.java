package de.mimirssource.accounting.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Balance.
 */
@Entity
@Table(name = "balance")
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @OneToOne
    @JoinColumn(unique = true)
    private Ledger ledger;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public Balance date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public Balance amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public Balance ledger(Ledger ledger) {
        this.ledger = ledger;
        return this;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Balance)) {
            return false;
        }
        return id != null && id.equals(((Balance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Balance{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
