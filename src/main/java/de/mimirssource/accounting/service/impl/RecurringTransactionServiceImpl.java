package de.mimirssource.accounting.service.impl;

import de.mimirssource.accounting.service.RecurringTransactionService;
import de.mimirssource.accounting.domain.RecurringTransaction;
import de.mimirssource.accounting.repository.RecurringTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RecurringTransaction}.
 */
@Service
@Transactional
public class RecurringTransactionServiceImpl implements RecurringTransactionService {

    private final Logger log = LoggerFactory.getLogger(RecurringTransactionServiceImpl.class);

    private final RecurringTransactionRepository recurringTransactionRepository;

    public RecurringTransactionServiceImpl(RecurringTransactionRepository recurringTransactionRepository) {
        this.recurringTransactionRepository = recurringTransactionRepository;
    }

    /**
     * Save a recurringTransaction.
     *
     * @param recurringTransaction the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RecurringTransaction save(RecurringTransaction recurringTransaction) {
        log.debug("Request to save RecurringTransaction : {}", recurringTransaction);
        return recurringTransactionRepository.save(recurringTransaction);
    }

    /**
     * Get all the recurringTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RecurringTransaction> findAll(Pageable pageable) {
        log.debug("Request to get all RecurringTransactions");
        return recurringTransactionRepository.findAll(pageable);
    }


    /**
     * Get one recurringTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecurringTransaction> findOne(Long id) {
        log.debug("Request to get RecurringTransaction : {}", id);
        return recurringTransactionRepository.findById(id);
    }

    /**
     * Delete the recurringTransaction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecurringTransaction : {}", id);
        recurringTransactionRepository.deleteById(id);
    }
}
