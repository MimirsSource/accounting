package de.mimirssource.accounting.repository;

import de.mimirssource.accounting.domain.Ledger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ledger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {

}
