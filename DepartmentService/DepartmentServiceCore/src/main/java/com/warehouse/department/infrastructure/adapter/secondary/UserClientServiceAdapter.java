package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.department.domain.port.secondary.UserClientServicePort;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class UserClientServiceAdapter implements UserClientServicePort {

    private final ApplicationEventPublisher eventPublisher;

    public UserClientServiceAdapter(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void update(final DepartmentSnapshot snapshot) {

    }
}
