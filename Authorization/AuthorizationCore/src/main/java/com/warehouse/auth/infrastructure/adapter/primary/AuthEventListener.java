package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.domain.model.AdminCreateRequest;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.infrastructure.adapter.primary.event.AdminUserCommand;
import com.warehouse.auth.infrastructure.adapter.primary.event.DepartmentUserDeleted;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthEventListener {

    private final AuthenticationPort authenticationPort;

    private final UserPort userPort;

    public AuthEventListener(final AuthenticationPort authenticationPort,
                             final UserPort userPort) {
        this.authenticationPort = authenticationPort;
        this.userPort = userPort;
    }

    @EventListener
    public void handle(final AdminUserCommand command) {
        log.info("Admin user command: {}", command.toString());
        final AdminCreateRequest request = new AdminCreateRequest(command.getDepartmentCode(),
                command.getEmail(), command.getTelephoneNumber());
        final UserId userId = this.authenticationPort.createAdminUser(request);
        command.getAdminCreatedId().accept(userId);
        log.info("Admin user created");
    }

    @EventListener
    public void handle(final DepartmentUserDeleted event) {
        final DepartmentCode departmentCode = event.getDepartmentCode();
        this.userPort.deleteDataForDepartment(departmentCode);
        log.info("Department user deleted");
    }
}
