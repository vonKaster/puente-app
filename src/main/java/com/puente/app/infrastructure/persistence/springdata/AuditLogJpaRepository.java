package com.puente.app.infrastructure.persistence.springdata;

import com.puente.app.domain.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogJpaRepository extends JpaRepository<AuditLog, Long> {
}

