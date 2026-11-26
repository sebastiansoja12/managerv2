package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoiceNumberSequenceEntity;
import com.warehouse.commonassets.identificator.InvoiceNumberSequenceId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvoiceNumberSequenceJpaRepository
        extends JpaRepository<InvoiceNumberSequenceEntity, InvoiceNumberSequenceId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sequence from billing.InvoiceNumberSequenceEntity sequence"
            + " where sequence.operatorId.value = :operatorId and sequence.type = :type"
            + " and sequence.period = :period")
    Optional<InvoiceNumberSequenceEntity> findForUpdate(@Param("operatorId") final Long operatorId,
                                                        @Param("type") final InvoiceType type,
                                                        @Param("period") final String period);
}
