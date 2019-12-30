package de.mimirssource.accounting.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

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
    @Column(name = "date", nullable = false)
    private Instant date;

    @OneToOne
    @JoinColumn(unique = true)
    private Ledger fromLedger;

    @OneToOne
    @JoinColumn(unique = true)
    private Ledger toLedger;

    @ManyToOne
    @JsonIgnoreProperties("transactions")
    private Category category;

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

    public Transaction name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public Transaction amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Instant getDate() {
        return date;
    }

    public Transaction date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Ledger getFromLedger() {
        return fromLedger;
    }

    public Transaction fromLedger(Ledger ledger) {
        this.fromLedger = ledger;
        return this;
    }

    public void setFromLedger(Ledger ledger) {
        this.fromLedger = ledger;
    }

    public Ledger getToLedger() {
        return toLedger;
    }

    public Transaction toLedger(Ledger ledger) {
        this.toLedger = ledger;
        return this;
    }

    public void setToLedger(Ledger ledger) {
        this.toLedger = ledger;
    }

    public Category getCategory() {
        return category;
    }

    public Transaction category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
