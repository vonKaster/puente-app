package com.puente.app.application;

import com.puente.app.domain.audit.AuditLog;
import com.puente.app.infrastructure.persistence.springdata.AuditLogJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditService {
    private final AuditLogJpaRepository auditLogRepository;

    public AuditService(AuditLogJpaRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String action, String performedBy, String details) {
        AuditLog log = new AuditLog(action, performedBy, details);
        auditLogRepository.save(log);
    }
}

