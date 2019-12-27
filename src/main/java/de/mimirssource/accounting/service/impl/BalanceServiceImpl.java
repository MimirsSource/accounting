package de.mimirssource.accounting.service.impl;

import de.mimirssource.accounting.service.BalanceService;
import de.mimirssource.accounting.domain.Balance;
import de.mimirssource.accounting.repository.BalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Balance}.
 */
@Service
@Transactional
public class BalanceServiceImpl implements BalanceService {

    private final Logger log = LoggerFactory.getLogger(BalanceServiceImpl.class);

    private final BalanceRepository balanceRepository;

    public BalanceServiceImpl(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    /**
     * Save a balance.
     *
     * @param balance the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Balance save(Balance balance) {
        log.debug("Request to save Balance : {}", balance);
        return balanceRepository.save(balance);
    }

    /**
     * Get all the balances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Balance> findAll(Pageable pageable) {
        log.debug("Request to get all Balances");
        return balanceRepository.findAll(pageable);
    }


    /**
     * Get one balance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Balance> findOne(Long id) {
        log.debug("Request to get Balance : {}", id);
        return balanceRepository.findById(id);
    }

    /**
     * Delete the balance by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Balance : {}", id);
        balanceRepository.deleteById(id);
    }
}
