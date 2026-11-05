package com.warehouse.auditlog.configuration;

import com.warehouse.auditlog.application.port.primary.AuditLogPort;
import com.warehouse.auditlog.application.port.primary.AuditLogPortImpl;
import com.warehouse.auditlog.application.port.secondary.AuditLogActorServicePort;
import com.warehouse.auditlog.application.port.secondary.AuditLogEntryRepository;
import com.warehouse.auditlog.infrastructure.adapter.primary.mapper.AuditLogEventMapper;
import com.warehouse.auditlog.infrastructure.adapter.primary.mapper.AuditLogWebMapper;
import com.warehouse.auditlog.infrastructure.adapter.secondary.AuditLogActorServiceAdapter;
import com.warehouse.auditlog.infrastructure.adapter.secondary.AuditLogEntryRepositoryImpl;
import com.warehouse.auditlog.infrastructure.adapter.secondary.entity.AuditLogEntryEntity;
import com.warehouse.auditlog.infrastructure.adapter.secondary.mapper.AuditLogEntryPersistenceMapper;
import com.warehouse.auth.UserApiService;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditLogConfiguration {

    @Bean("auditLogEntryBaseRepository")
    public OperatorFilteredRepository<AuditLogEntryEntity> auditLogEntryBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public AuditLogEntryRepository auditLogEntryRepository(
            @Qualifier("auditLogEntryBaseRepository")
            final OperatorFilteredRepository<AuditLogEntryEntity> repository) {
        return new AuditLogEntryRepositoryImpl(repository, new AuditLogEntryPersistenceMapper());
    }

    @Bean
    public AuditLogActorServicePort auditLogActorServicePort(final OperatorContextProvider operatorContextProvider,
                                                             final UserApiService userApiService) {
        return new AuditLogActorServiceAdapter(operatorContextProvider, userApiService);
    }

    @Bean
    public AuditLogPort auditLogPort(final AuditLogEntryRepository auditLogEntryRepository,
                                     final AuditLogActorServicePort auditLogActorServicePort) {
        return new AuditLogPortImpl(auditLogEntryRepository, auditLogActorServicePort);
    }

    @Bean
    public AuditLogWebMapper auditLogWebMapper() {
        return new AuditLogWebMapper();
    }

    @Bean
    public AuditLogEventMapper auditLogEventMapper() {
        return new AuditLogEventMapper();
    }
}
