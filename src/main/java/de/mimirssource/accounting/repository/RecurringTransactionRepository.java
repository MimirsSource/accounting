package de.mimirssource.accounting.repository;

import de.mimirssource.accounting.domain.RecurringTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RecurringTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {

}
