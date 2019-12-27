package de.mimirssource.accounting.service;

import de.mimirssource.accounting.domain.RecurringTransaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link RecurringTransaction}.
 */
public interface RecurringTransactionService {

    /**
     * Save a recurringTransaction.
     *
     * @param recurringTransaction the entity to save.
     * @return the persisted entity.
     */
    RecurringTransaction save(RecurringTransaction recurringTransaction);

    /**
     * Get all the recurringTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecurringTransaction> findAll(Pageable pageable);


    /**
     * Get the "id" recurringTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecurringTransaction> findOne(Long id);

    /**
     * Delete the "id" recurringTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
