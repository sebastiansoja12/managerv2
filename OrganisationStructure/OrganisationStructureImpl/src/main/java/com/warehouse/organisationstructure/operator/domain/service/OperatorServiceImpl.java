package com.warehouse.organisationstructure.operator.domain.service;

import java.time.Instant;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.event.OperatorChangedEvent;
import com.warehouse.organisationstructure.operator.domain.event.OperatorCreatedEvent;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorRepository;
import com.warehouse.organisationstructure.operator.domain.registry.DomainContext;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;

import java.util.List;

public class OperatorServiceImpl implements OperatorService {

    private static final long MIN_OPERATOR_ID = 10000L;
    private static final long MAX_OPERATOR_ID = 99999L;

    private final OperatorRepository operatorRepository;
    private final OperatorConfigurationService operatorConfigurationService;

    public OperatorServiceImpl(final OperatorRepository operatorRepository,
                               final OperatorConfigurationService operatorConfigurationService) {
        this.operatorRepository = operatorRepository;
        this.operatorConfigurationService = operatorConfigurationService;
    }

    @Override
    public List<Operator> getAll() {
        return operatorRepository.findAll()
                .stream()
                .map(this::withConfiguration)
                .toList();
    }

    @Override
    public Operator getById(final OperatorId operatorId) {
        return operatorRepository.findById(operatorId)
                .map(this::withConfiguration)
                .orElseThrow(() -> new IllegalArgumentException("Operator not found: " + operatorId));
    }

    @Override
    public boolean exists(final OperatorId operatorId) {
        return operatorRepository.existsById(operatorId);
    }

    @Override
    public boolean isActive(final OperatorId operatorId) {
        return operatorRepository.findById(operatorId)
                .map(Operator::isActive)
                .orElse(false);
    }

    @Override
    @Transactional
    public void create(final Operator operator) {
        this.operatorRepository.save(operator);
        DomainContext.eventPublisher().publishEvent(new OperatorCreatedEvent(operator.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public Operator update(final OperatorId operatorId, final UpdateOperatorCommand command) {
        final Operator operator = getById(operatorId);
        operator.setTaxId(command.taxId());
        operator.setSupportsLockers(command.supportsLockers());
        operator.setSupportsInternationalShipping(command.supportsInternationalShipping());
        operator.setSupportsCashOnDelivery(command.supportsCashOnDelivery());
        operator.setContactPhone(command.contactPhone());
        operator.setContactEmail(command.contactEmail());
        operator.setCompanyName(command.companyName());
        operator.setContractStartDate(command.contractStartDate());
        operator.setContractEndDate(command.contractEndDate());
        operator.setFoundedDate(command.foundedDate());
        operator.setConfiguration(command.configuration());
        operator.setStatus(command.status());
        operator.setUpdatedAt(Instant.now());

        this.operatorRepository.save(operator);
        operatorConfigurationService.create(operatorId, command.configuration());
        DomainContext.eventPublisher().publishEvent(new OperatorChangedEvent(operator.snapshot(), Instant.now()));
        return getById(operatorId);
    }

    @Override
    public synchronized OperatorId nextOperatorId() {
        long nextValue = operatorRepository.findMaxOperatorIdValue()
                .map(value -> Math.max(value + 1, MIN_OPERATOR_ID))
                .orElse(MIN_OPERATOR_ID);

        while (nextValue <= MAX_OPERATOR_ID) {
            final OperatorId operatorId = OperatorId.of(nextValue);
            if (!operatorRepository.existsById(operatorId)) {
                return operatorId;
            }
            nextValue++;
        }

        throw new IllegalStateException("No available 5 digit operator id");
    }

    @Override
    public void assignRegisteringUser(final OperatorId operatorId, final UserId userId) {
        final Operator operator = getById(operatorId);
        operator.assignRegisteringUser(userId);
        this.operatorRepository.save(operator);
    }

    private Operator withConfiguration(final Operator operator) {
        operatorConfigurationService.getByOperatorId(operator.getOperatorId())
                .ifPresent(operator::setConfiguration);
        return operator;
    }
}
