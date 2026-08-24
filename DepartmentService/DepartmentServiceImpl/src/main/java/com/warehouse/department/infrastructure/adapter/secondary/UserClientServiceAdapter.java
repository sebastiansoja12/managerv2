package com.warehouse.department.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.Map;

import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.department.domain.port.secondary.UserClientServicePort;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import com.warehouse.department.infrastructure.adapter.secondary.event.DepartmentUserChanged;
import com.warehouse.department.infrastructure.adapter.secondary.event.DepartmentUserDeleted;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserClientServiceAdapter implements UserClientServicePort {

    private static final String KAFKA_TYPE_ID = "__TypeId__";

    private final KafkaTemplateClient kafkaTemplateClient;
    private final String departmentUserEventsTopic;

    public UserClientServiceAdapter(final KafkaTemplateClient kafkaTemplateClient,
                                    final String departmentUserEventsTopic) {
        this.kafkaTemplateClient = kafkaTemplateClient;
        this.departmentUserEventsTopic = departmentUserEventsTopic;
    }

    @Override
    public void notifyUserDepartmentDeleted(final DepartmentSnapshot snapshot) {
        final DepartmentUserDeleted event = new DepartmentUserDeleted(snapshot.departmentCode(), Instant.now());
        this.publish(snapshot, event);
        log.info("Notifying user department deleted event for department {}", snapshot.departmentCode());
    }

    @Override
    public void notifyUserDepartmentChanged(final DepartmentSnapshot snapshot) {
        final DepartmentUserChanged event = new DepartmentUserChanged(
                snapshot.departmentCode(),
                snapshot.adminUserId(),
                snapshot.telephoneNumber(),
                snapshot.email(),
                Instant.now()
        );
        this.publish(snapshot, event);
        log.info("Notifying user data changed for department: {}", snapshot.departmentCode());
    }

    private void publish(final DepartmentSnapshot snapshot, final Object event) {
        this.kafkaTemplateClient.publish(
                this.departmentUserEventsTopic,
                snapshot.departmentCode().getValue(),
                event,
                Map.of(
                        KAFKA_TYPE_ID, event.getClass().getSimpleName(),
                        KafkaEventHeaders.EVENT_TYPE, event.getClass().getSimpleName()
                )
        );
    }
}
