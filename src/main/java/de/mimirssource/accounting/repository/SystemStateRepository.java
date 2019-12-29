package de.mimirssource.accounting.repository;

import de.mimirssource.accounting.domain.SystemState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemStateRepository extends JpaRepository<SystemState, SystemState.SystemKey> {
}
