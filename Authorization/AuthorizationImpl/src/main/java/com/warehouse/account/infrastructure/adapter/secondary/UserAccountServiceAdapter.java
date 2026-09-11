package com.warehouse.account.infrastructure.adapter.secondary;

import com.warehouse.account.application.port.secondary.NewSupplierAccount;
import com.warehouse.account.application.port.secondary.UserAccountServicePort;
import com.warehouse.auth.domain.model.CreateSupplierUserCommand;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.UserNotFoundException;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserAccountServiceAdapter implements UserAccountServicePort {

    private final AuthenticationPort authenticationPort;
    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;
    private final OperatorContext operatorContext;

    public UserAccountServiceAdapter(final AuthenticationPort authenticationPort,
                                     final UserPort userPort,
                                     final PasswordEncoder passwordEncoder,
                                     final OperatorContext operatorContext) {
        this.authenticationPort = authenticationPort;
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.operatorContext = operatorContext;
    }

    @Override
    public boolean usernameExists(final String username) {
        try {
            return this.userPort.findUser(username) != null;
        } catch (final UserNotFoundException exception) {
            return false;
        }
    }

    @Override
    public UserId createSupplierUser(final NewSupplierAccount account) {
        return this.authenticationPort.createSupplierUser(new CreateSupplierUserCommand(
                account.username(),
                account.firstName(),
                account.lastName(),
                account.email(),
                account.departmentId(),
                account.language()));
    }

    @Override
    public void changePassword(final OperatorId operatorId, final UserId userId, final String rawPassword) {
        final String encodedPassword = this.passwordEncoder.encode(rawPassword);
        this.operatorContext.runAs(operatorId, userId, () -> this.userPort.changePassword(userId, encodedPassword));
    }
}
