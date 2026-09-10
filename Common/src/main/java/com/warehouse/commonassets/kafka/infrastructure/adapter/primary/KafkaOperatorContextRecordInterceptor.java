package com.warehouse.commonassets.kafka.infrastructure.adapter.primary;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.listener.RecordInterceptor;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;

public class KafkaOperatorContextRecordInterceptor implements RecordInterceptor<String, String> {

    private final OperatorContext operatorContext;

    public KafkaOperatorContextRecordInterceptor(final OperatorContext operatorContext) {
        this.operatorContext = operatorContext;
    }

    @Override
    public ConsumerRecord<String, String> intercept(final ConsumerRecord<String, String> record,
                                                   final Consumer<String, String> consumer) {
        operatorId(record).ifPresent(operatorId -> this.operatorContext.assignOperatorContext(
                operatorId,
                userId(record).orElse(null),
                departmentId(record).orElse(null)));
        return record;
    }

    @Override
    public void afterRecord(final ConsumerRecord<String, String> record,
                            final Consumer<String, String> consumer) {
        this.operatorContext.clear();
    }

    private Optional<OperatorId> operatorId(final ConsumerRecord<String, String> record) {
        return identifier(record, KafkaEventHeaders.OPERATOR_ID).map(OperatorId::of);
    }

    private Optional<UserId> userId(final ConsumerRecord<String, String> record) {
        return identifier(record, KafkaEventHeaders.USER_ID).map(UserId::new);
    }

    private Optional<DepartmentId> departmentId(final ConsumerRecord<String, String> record) {
        return identifier(record, KafkaEventHeaders.DEPARTMENT_ID).map(DepartmentId::new);
    }

    private Optional<Long> identifier(final ConsumerRecord<String, String> record, final String headerName) {
        final Header header = record.headers().lastHeader(headerName);
        if (header == null) {
            return Optional.empty();
        }

        return Optional.of(Long.valueOf(new String(header.value(), StandardCharsets.UTF_8)));
    }
}
