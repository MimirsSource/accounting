package de.mimirssource.accounting.balancing;

import de.mimirssource.accounting.domain.RecurringTransaction;
import de.mimirssource.accounting.domain.SystemState;
import de.mimirssource.accounting.domain.Transaction;
import de.mimirssource.accounting.repository.RecurringTransactionRepository;
import de.mimirssource.accounting.repository.SystemStateRepository;
import de.mimirssource.accounting.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

@Component
public class RecurringTransactionManager {

    private SystemStateRepository systemStateRepository;

    private RecurringTransactionRepository recurringTransactionRepository;

    private TransactionRepository transactionRepository;

    @Autowired
    public RecurringTransactionManager(
        final SystemStateRepository systemStateRepository,
        RecurringTransactionRepository recurringTransactionRepository,
        TransactionRepository transactionRepository) {
        this.systemStateRepository = systemStateRepository;
        this.recurringTransactionRepository = recurringTransactionRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void applyRecurringTransactions() {
        // TODO  must always be set to an initial value
        final Instant from =
            this.systemStateRepository.findById(SystemState.SystemKey.RECURRING_APPLIED_UNTIL).get().getDate();
        final List<RecurringTransaction> recurringTransactions = this.recurringTransactionRepository.findAll();
        final Instant now = Instant.now();

        List<Transaction> transactions = new ArrayList<Transaction>();
        while(from.isBefore(now)) {
            for(RecurringTransaction recurringTransaction : recurringTransactions) {
                if(recurringTransaction.getDateOfMonth().equals(MonthDay.from(from).getDayOfMonth())) {
                    transactions.add(this.createFromRecurring(from, recurringTransaction));
                }
            }
            from.plus(Duration.ofDays(1));
        }

        this.transactionRepository.saveAll(transactions);
    }

    private Transaction createFromRecurring(final Instant instant,
                                            final RecurringTransaction recurringTransaction) {
        return new Transaction().amount(recurringTransaction.getAmount())
            .category(recurringTransaction.getCategory())
            .date(Instant.ofEpochSecond(instant.getEpochSecond()))
            .fromLedger(recurringTransaction.getFromLedger())
            .toLedger(recurringTransaction.getToLedger())
            .name(recurringTransaction.getName());
    }


}
