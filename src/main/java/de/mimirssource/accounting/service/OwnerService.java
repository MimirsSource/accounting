package de.mimirssource.accounting.service;

import de.mimirssource.accounting.domain.Owner;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Owner}.
 */
public interface OwnerService {

    /**
     * Save a owner.
     *
     * @param owner the entity to save.
     * @return the persisted entity.
     */
    Owner save(Owner owner);

    /**
     * Get all the owners.
     *
     * @return the list of entities.
     */
    List<Owner> findAll();


    /**
     * Get the "id" owner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Owner> findOne(Long id);

    /**
     * Delete the "id" owner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
