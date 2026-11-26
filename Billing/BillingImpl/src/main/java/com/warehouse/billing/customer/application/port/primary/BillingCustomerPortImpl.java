package com.warehouse.billing.customer.application.port.primary;

import com.warehouse.billing.customer.application.port.primary.command.CreateBillingCustomerCommand;
import com.warehouse.billing.customer.application.port.primary.command.UpdateBillingCustomerCommand;
import com.warehouse.billing.customer.application.port.secondary.BillingCustomerRepository;
import com.warehouse.billing.customer.application.port.secondary.BillingCustomerUserServicePort;
import com.warehouse.billing.customer.domain.exception.BillingCustomerException;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.billing.customer.domain.service.BillingCustomerFactory;
import com.warehouse.billing.customer.domain.vo.BillingCustomerAccount;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

public class BillingCustomerPortImpl implements BillingCustomerPort {

    private final BillingCustomerRepository billingCustomerRepository;

    private final BillingCustomerUserServicePort billingCustomerUserServicePort;

    private final BillingCustomerFactory billingCustomerFactory;

    private final Clock clock;

    public BillingCustomerPortImpl(final BillingCustomerRepository billingCustomerRepository,
                                   final BillingCustomerUserServicePort billingCustomerUserServicePort,
                                   final BillingCustomerFactory billingCustomerFactory,
                                   final Clock clock) {
        this.billingCustomerRepository = billingCustomerRepository;
        this.billingCustomerUserServicePort = billingCustomerUserServicePort;
        this.billingCustomerFactory = billingCustomerFactory;
        this.clock = clock;
    }

    @Override
    @Transactional
    public BillingCustomer create(final CreateBillingCustomerCommand command) {
        if (this.billingCustomerRepository.findByTaxId(command.taxId()).isPresent()) {
            throw new BillingCustomerException(BillingCustomerException.Reason.DUPLICATE,
                    "Business customer with this tax id already exists");
        }
        final OperatorId operatorId = this.billingCustomerUserServicePort.currentOperatorId();
        final BillingCustomerAccount account = this.billingCustomerUserServicePort.createUser(
                this.billingCustomerFactory.username(operatorId, command.taxId()), command.details().companyName(),
                this.billingCustomerFactory.userEmail(operatorId, command.taxId()));
        final BillingCustomer billingCustomer = this.billingCustomerFactory.create(command.taxId(),
                command.details(), account, this.clock.instant());
        this.billingCustomerRepository.create(billingCustomer);
        return billingCustomer;
    }

    @Override
    @Transactional
    public BillingCustomer update(final UpdateBillingCustomerCommand command) {
        final BillingCustomer billingCustomer = get(command.billingCustomerId());
        billingCustomer.update(command.details(), this.clock.instant());
        this.billingCustomerRepository.update(billingCustomer);
        return billingCustomer;
    }

    @Override
    public List<BillingCustomer> findAll() {
        return this.billingCustomerRepository.findAll();
    }

    @Override
    public BillingCustomer get(final BillingCustomerId billingCustomerId) {
        return this.billingCustomerRepository.findById(billingCustomerId)
                .orElseThrow(() -> new BillingCustomerException(BillingCustomerException.Reason.NOT_FOUND,
                        "Business customer was not found"));
    }

    @Override
    public Optional<BillingCustomer> findById(final BillingCustomerId billingCustomerId) {
        return this.billingCustomerRepository.findById(billingCustomerId);
    }

    @Override
    public Optional<BillingCustomer> findByUser(final UserId userId) {
        return this.billingCustomerRepository.findByUser(userId);
    }

    @Override
    public BillingCustomer currentActive() {
        final BillingCustomer billingCustomer = this.billingCustomerRepository
                .findByUser(this.billingCustomerUserServicePort.currentUserId())
                .orElseThrow(() -> new BillingCustomerException(BillingCustomerException.Reason.NOT_ACTIVE,
                        "Business customer is not active"));
        billingCustomer.ensureActive();
        return billingCustomer;
    }

    @Override
    @Transactional
    public String regenerateApiKey(final BillingCustomerId billingCustomerId) {
        return this.billingCustomerUserServicePort.regenerateApiKey(get(billingCustomerId).userId());
    }

    @Override
    @Transactional
    public BillingCustomer block(final BillingCustomerId billingCustomerId) {
        final BillingCustomer billingCustomer = get(billingCustomerId);
        billingCustomer.block(this.clock.instant());
        this.billingCustomerRepository.update(billingCustomer);
        return billingCustomer;
    }

    @Override
    @Transactional
    public BillingCustomer unblock(final BillingCustomerId billingCustomerId) {
        final BillingCustomer billingCustomer = get(billingCustomerId);
        billingCustomer.unblock(this.clock.instant());
        this.billingCustomerRepository.update(billingCustomer);
        return billingCustomer;
    }
}
