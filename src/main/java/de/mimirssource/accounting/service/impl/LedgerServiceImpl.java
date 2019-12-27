package de.mimirssource.accounting.service.impl;

import de.mimirssource.accounting.service.LedgerService;
import de.mimirssource.accounting.domain.Ledger;
import de.mimirssource.accounting.repository.LedgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Ledger}.
 */
@Service
@Transactional
public class LedgerServiceImpl implements LedgerService {

    private final Logger log = LoggerFactory.getLogger(LedgerServiceImpl.class);

    private final LedgerRepository ledgerRepository;

    public LedgerServiceImpl(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    /**
     * Save a ledger.
     *
     * @param ledger the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Ledger save(Ledger ledger) {
        log.debug("Request to save Ledger : {}", ledger);
        return ledgerRepository.save(ledger);
    }

    /**
     * Get all the ledgers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Ledger> findAll(Pageable pageable) {
        log.debug("Request to get all Ledgers");
        return ledgerRepository.findAll(pageable);
    }


    /**
     * Get one ledger by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ledger> findOne(Long id) {
        log.debug("Request to get Ledger : {}", id);
        return ledgerRepository.findById(id);
    }

    /**
     * Delete the ledger by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ledger : {}", id);
        ledgerRepository.deleteById(id);
    }
}
