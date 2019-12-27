package de.mimirssource.accounting.web.rest;

import de.mimirssource.accounting.domain.RecurringTransaction;
import de.mimirssource.accounting.service.RecurringTransactionService;
import de.mimirssource.accounting.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.mimirssource.accounting.domain.RecurringTransaction}.
 */
@RestController
@RequestMapping("/api")
public class RecurringTransactionResource {

    private final Logger log = LoggerFactory.getLogger(RecurringTransactionResource.class);

    private static final String ENTITY_NAME = "recurringTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecurringTransactionService recurringTransactionService;

    public RecurringTransactionResource(RecurringTransactionService recurringTransactionService) {
        this.recurringTransactionService = recurringTransactionService;
    }

    /**
     * {@code POST  /recurring-transactions} : Create a new recurringTransaction.
     *
     * @param recurringTransaction the recurringTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recurringTransaction, or with status {@code 400 (Bad Request)} if the recurringTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recurring-transactions")
    public ResponseEntity<RecurringTransaction> createRecurringTransaction(@Valid @RequestBody RecurringTransaction recurringTransaction) throws URISyntaxException {
        log.debug("REST request to save RecurringTransaction : {}", recurringTransaction);
        if (recurringTransaction.getId() != null) {
            throw new BadRequestAlertException("A new recurringTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecurringTransaction result = recurringTransactionService.save(recurringTransaction);
        return ResponseEntity.created(new URI("/api/recurring-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recurring-transactions} : Updates an existing recurringTransaction.
     *
     * @param recurringTransaction the recurringTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recurringTransaction,
     * or with status {@code 400 (Bad Request)} if the recurringTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recurringTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recurring-transactions")
    public ResponseEntity<RecurringTransaction> updateRecurringTransaction(@Valid @RequestBody RecurringTransaction recurringTransaction) throws URISyntaxException {
        log.debug("REST request to update RecurringTransaction : {}", recurringTransaction);
        if (recurringTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecurringTransaction result = recurringTransactionService.save(recurringTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recurringTransaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /recurring-transactions} : get all the recurringTransactions.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recurringTransactions in body.
     */
    @GetMapping("/recurring-transactions")
    public ResponseEntity<List<RecurringTransaction>> getAllRecurringTransactions(Pageable pageable) {
        log.debug("REST request to get a page of RecurringTransactions");
        Page<RecurringTransaction> page = recurringTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recurring-transactions/:id} : get the "id" recurringTransaction.
     *
     * @param id the id of the recurringTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recurringTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recurring-transactions/{id}")
    public ResponseEntity<RecurringTransaction> getRecurringTransaction(@PathVariable Long id) {
        log.debug("REST request to get RecurringTransaction : {}", id);
        Optional<RecurringTransaction> recurringTransaction = recurringTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recurringTransaction);
    }

    /**
     * {@code DELETE  /recurring-transactions/:id} : delete the "id" recurringTransaction.
     *
     * @param id the id of the recurringTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recurring-transactions/{id}")
    public ResponseEntity<Void> deleteRecurringTransaction(@PathVariable Long id) {
        log.debug("REST request to delete RecurringTransaction : {}", id);
        recurringTransactionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
