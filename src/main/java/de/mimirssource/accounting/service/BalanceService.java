package de.mimirssource.accounting.service;

import de.mimirssource.accounting.domain.Balance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Balance}.
 */
public interface BalanceService {

    /**
     * Save a balance.
     *
     * @param balance the entity to save.
     * @return the persisted entity.
     */
    Balance save(Balance balance);

    /**
     * Get all the balances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Balance> findAll(Pageable pageable);


    /**
     * Get the "id" balance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Balance> findOne(Long id);

    /**
     * Delete the "id" balance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
