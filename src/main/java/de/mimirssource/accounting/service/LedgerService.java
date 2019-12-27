package de.mimirssource.accounting.service;

import de.mimirssource.accounting.domain.Ledger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Ledger}.
 */
public interface LedgerService {

    /**
     * Save a ledger.
     *
     * @param ledger the entity to save.
     * @return the persisted entity.
     */
    Ledger save(Ledger ledger);

    /**
     * Get all the ledgers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ledger> findAll(Pageable pageable);


    /**
     * Get the "id" ledger.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ledger> findOne(Long id);

    /**
     * Delete the "id" ledger.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
