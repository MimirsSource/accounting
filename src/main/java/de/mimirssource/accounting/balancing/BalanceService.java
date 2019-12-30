package de.mimirssource.accounting.balancing;

import de.mimirssource.accounting.domain.Balance;
import de.mimirssource.accounting.domain.Ledger;
import de.mimirssource.accounting.domain.SystemState;
import de.mimirssource.accounting.domain.Transaction;
import de.mimirssource.accounting.repository.BalanceRepository;
import de.mimirssource.accounting.repository.LedgerRepository;
import de.mimirssource.accounting.repository.SystemStateRepository;
import de.mimirssource.accounting.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BalanceService {

    private final SystemStateRepository systemStateRepository;
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final LedgerRepository ledgerRepository;

    public BalanceService(final SystemStateRepository systemStateRepository,
                          final BalanceRepository balanceRepository,
                          final TransactionRepository transactionRepository,
                          final LedgerRepository ledgerRepository) {
        this.systemStateRepository = systemStateRepository;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.ledgerRepository = ledgerRepository;
    }

    @Transactional
    public void createNextBalance() {
        final Optional<SystemState> lastBalanceOpt =
            this.systemStateRepository.findById(SystemState.SystemKey.LAST_BALANCE);

        if(!lastBalanceOpt.isPresent()) {
            // TODO own exception
            throw new IllegalStateException("Cannot run balancing without an initial balance.");
        }
        SystemState lastBalance = lastBalanceOpt.get();

        Instant newBalanceInstant = lastBalance.getDate().plus(Period.ofMonths(1));

        final List<Balance> lastBalances = this.balanceRepository.findByDate(lastBalance.getDate());
        List<Transaction> transactions =
            this.transactionRepository.findAllByDateBetween(lastBalance.getDate(), newBalanceInstant);
        final List<Ledger> ledgers = this.ledgerRepository.findAll();

        List<Balance> newBalances = new ArrayList<Balance>(ledgers.size());

        for(Ledger ledger : ledgers) {
            AtomicInteger amount = new AtomicInteger();
            // initial amount
            lastBalances.stream().filter(balance -> balance.getLedger().getId() == ledger.getId())
                .findFirst().ifPresent( balance -> amount.addAndGet(balance.getAmount()));

            // calculate transactions
            transactions.stream().forEach(transaction -> {
                if(transaction.getToLedger().getId() == ledger.getId()) {
                    amount.addAndGet(transaction.getAmount());
                }
                if(transaction.getFromLedger().getId() == ledger.getId()) {
                    amount.addAndGet(-transaction.getAmount());
                }
            });

            newBalances.add(new Balance().amount(amount.get()).date(newBalanceInstant).ledger(ledger));
        }

        this.balanceRepository.saveAll(newBalances);
        lastBalance.setDate(newBalanceInstant);
        this.systemStateRepository.save(lastBalance);
    }
}
