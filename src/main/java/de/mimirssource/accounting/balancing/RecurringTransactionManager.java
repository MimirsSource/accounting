package de.mimirssource.accounting.balancing;

import de.mimirssource.accounting.domain.RecurringTransaction;
import de.mimirssource.accounting.domain.SystemState;
import de.mimirssource.accounting.repository.RecurringTransactionRepository;
import de.mimirssource.accounting.repository.SystemStateRepository;
import de.mimirssource.accounting.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    public void applyRecurringTransactions() {
        final Optional<SystemState> until =
            this.systemStateRepository.findById(SystemState.SystemKey.RECURRING_APPLIED_UNTIL);
        SystemState stateUntil = until.isPresent() ? until.get() : SystemState.noRecurringTransactionsYet();

        final List<RecurringTransaction> recurringTransactions = this.recurringTransactionRepository.findAll();




    }


}
