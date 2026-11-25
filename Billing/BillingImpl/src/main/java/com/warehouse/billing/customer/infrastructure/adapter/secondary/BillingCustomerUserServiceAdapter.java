package com.warehouse.billing.customer.infrastructure.adapter.secondary;

import com.warehouse.auth.UserApiService;
import com.warehouse.auth.infrastructure.dto.CreateBusinessCustomerUserApiRequest;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.billing.customer.application.port.secondary.BillingCustomerUserServicePort;
import com.warehouse.billing.customer.domain.vo.BillingCustomerAccount;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;

public class BillingCustomerUserServiceAdapter implements BillingCustomerUserServicePort {

    private static final String LANGUAGE = "pl";

    private final UserApiService userApiService;

    private final OperatorContextProvider operatorContextProvider;

    public BillingCustomerUserServiceAdapter(final UserApiService userApiService,
                                             final OperatorContextProvider operatorContextProvider) {
        this.userApiService = userApiService;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public BillingCustomerAccount createUser(final String username, final String name, final String email) {
        final UserDto user = this.userApiService.createBusinessCustomerUser(
                new CreateBusinessCustomerUserApiRequest(username, name, email, LANGUAGE));
        return new BillingCustomerAccount(new UserId(user.userId().value()), new OperatorId(user.operatorId().value()));
    }

    @Override
    public String regenerateApiKey(final UserId userId) {
        return this.userApiService.regenerateApiKey(userId);
    }

    @Override
    public UserId currentUserId() {
        return this.operatorContextProvider.currentUserId();
    }

    @Override
    public OperatorId currentOperatorId() {
        return this.operatorContextProvider.currentOperatorId();
    }
}
