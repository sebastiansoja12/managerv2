package com.warehouse.department.infrastructure.adapter.secondary;

import java.time.Instant;

import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.department.domain.port.secondary.UserClientServicePort;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import com.warehouse.department.infrastructure.adapter.secondary.event.DepartmentUserChanged;
import com.warehouse.department.infrastructure.adapter.secondary.event.DepartmentUserDeleted;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserClientServiceAdapter implements UserClientServicePort {

    private final KafkaTemplateClient kafkaTemplateClient;

    public UserClientServiceAdapter(final KafkaTemplateClient kafkaTemplateClient) {
        this.kafkaTemplateClient = kafkaTemplateClient;
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
        this.kafkaTemplateClient.publish(snapshot.departmentCode().getValue(), event);
    }
}
