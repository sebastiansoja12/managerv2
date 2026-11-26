package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.application.port.secondary.LateChargeRefundRepository;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.LateChargeRefundEntity;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.PaymentEntity;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.mapper.PaymentPersistenceMapper;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.NoOperatorContextBaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@AutoConfigurationPackage(basePackageClasses = PaymentEntity.class)
@EntityScan(basePackageClasses = PaymentEntity.class)
public class PaymentPersistenceTestConfiguration {

    public static final OperatorId OPERATOR_ID = OperatorId.of(10L);

    @Bean
    public OperatorContextProvider operatorContextProvider() {
        return () -> Optional.of(new OperatorDetails(OPERATOR_ID, new UserId(7L), new DepartmentId(100L)));
    }

    @Bean
    public PaymentRepository paymentRepository(final EntityManager entityManager,
                                               final OperatorContextProvider operatorContextProvider) {
        return new PaymentRepositoryImpl(new BaseRepository<PaymentEntity>(entityManager, operatorContextProvider),
                new NoOperatorContextBaseRepository<>(entityManager), new PaymentPersistenceMapper());
    }

    @Bean
    public LateChargeRefundRepository lateChargeRefundRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new LateChargeRefundRepositoryImpl(
                new BaseRepository<LateChargeRefundEntity>(entityManager, operatorContextProvider),
                new NoOperatorContextBaseRepository<>(entityManager));
    }
}
