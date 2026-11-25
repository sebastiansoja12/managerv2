package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.warehouse.billing.invoice.application.port.secondary.InvoiceNumberSequenceRepository;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceRepository;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoiceEntity;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoiceNumberSequenceEntity;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.mapper.InvoicePersistenceMapper;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
@AutoConfigurationPackage(basePackageClasses = InvoiceRepositoryImpl.class)
@EntityScan(basePackageClasses = InvoiceEntity.class)
public class InvoicePersistenceTestConfiguration {

    public static final OperatorId OPERATOR_ID = OperatorId.of(10L);

    public static final AtomicReference<OperatorId> CURRENT_OPERATOR = new AtomicReference<>(OPERATOR_ID);

    @Bean
    public OperatorContextProvider operatorContextProvider() {
        return () -> Optional.of(new OperatorDetails(CURRENT_OPERATOR.get(), new UserId(7L), new DepartmentId(100L)));
    }

    @Bean
    public InvoiceRepository invoiceRepository(final EntityManager entityManager,
                                               final OperatorContextProvider operatorContextProvider) {
        return new InvoiceRepositoryImpl(new BaseRepository<InvoiceEntity>(entityManager, operatorContextProvider),
                new InvoicePersistenceMapper());
    }

    @Bean
    public InvoiceNumberSequenceRepository invoiceNumberSequenceRepository(
            final InvoiceNumberSequenceJpaRepository invoiceNumberSequenceJpaRepository,
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new InvoiceNumberSequenceRepositoryImpl(invoiceNumberSequenceJpaRepository,
                new BaseRepository<InvoiceNumberSequenceEntity>(entityManager, operatorContextProvider),
                operatorContextProvider);
    }
}
